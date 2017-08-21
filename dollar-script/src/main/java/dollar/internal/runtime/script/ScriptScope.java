/*
 *    Copyright (c) 2014-2017 Neil Ellis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dollar.internal.runtime.script;

import com.sillelien.dollar.api.DollarException;
import com.sillelien.dollar.api.Pipeable;
import com.sillelien.dollar.api.collections.MultiHashMap;
import com.sillelien.dollar.api.collections.MultiMap;
import com.sillelien.dollar.api.var;
import dollar.internal.runtime.script.api.Scope;
import dollar.internal.runtime.script.api.Variable;
import dollar.internal.runtime.script.api.exceptions.DollarAssertionException;
import dollar.internal.runtime.script.api.exceptions.DollarParserError;
import dollar.internal.runtime.script.api.exceptions.DollarScriptException;
import dollar.internal.runtime.script.api.exceptions.VariableNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jparsec.Parser;
import org.jparsec.error.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sillelien.dollar.api.DollarStatic.*;

public class ScriptScope implements Scope {

    @NotNull
    private static final Logger log = LoggerFactory.getLogger("ScriptScope");

    @NotNull
    private static final AtomicInteger counter = new AtomicInteger();
    @NotNull
    protected final ConcurrentHashMap<String, Variable> variables = new ConcurrentHashMap<>();

    @NotNull
    final String id;
    @NotNull
    private final MultiMap<String, Listener> listeners = new MultiHashMap<>();
    @NotNull
    private final List<var> errorHandlers = new CopyOnWriteArrayList<>();
    private final boolean root;
    @Nullable
    Scope parent;
    @Nullable
    private String source;
    @Nullable
    private String file;
    @NotNull
    private Parser<var> parser;

    private boolean parameterScope;
    private boolean destroyed;

    public ScriptScope(@NotNull String name, boolean root) {
        this.root = root;
        parent = null;
        file = null;
        source = null;
        id = name + ":" + counter.incrementAndGet();
    }

    public ScriptScope(@NotNull String source, @NotNull String name, boolean root) {
        this.root = root;
        parent = null;
        this.source = source;
        file = null;

        id = name + ":" + counter.incrementAndGet();
    }


    public ScriptScope(@NotNull Scope parent, @NotNull String name, boolean root) {
        this.parent = parent;
        file = parent.getFile();
        this.root = root;
        source = parent.getSource();
        id = name + ":" + counter.incrementAndGet();
    }

    public ScriptScope(@NotNull Scope parent,
                       @NotNull String file,
                       @Nullable String source,
                       @NotNull String name,
                       boolean root) {
        this.parent = parent;
        this.file = file;
        this.root = root;
        if (source == null) {
            throw new NullPointerException("No source for " + parent);
        } else {
            this.source = source;

        }

        id = name + ":" + counter.incrementAndGet();
    }

    public ScriptScope(@Nullable String source, @NotNull File file, boolean root) {
        this.source = source;
        this.file = file.getAbsolutePath();
        this.root = root;
        id = "(file-scope):" + counter.incrementAndGet();
    }

    public ScriptScope(@NotNull Scope parent,
                       @NotNull String id,
                       @NotNull String file,
                       boolean parameterScope,
                       @NotNull ConcurrentHashMap<String, Variable> variables,
                       @NotNull List<var> errorHandlers,
                       @NotNull MultiMap<String, Listener> listeners,
                       @NotNull String source,
                       @NotNull Parser<var> parser, boolean root) {
        this.parent = parent;
        this.id = id;
        this.file = file;
        this.parameterScope = parameterScope;
        this.variables.putAll(variables);
        this.errorHandlers.addAll(errorHandlers);
        for (Map.Entry<String, Collection<Listener>> entry : listeners.entries()) {
            this.listeners.putAll(entry.getKey(), entry.getValue());
        }
        this.source = source;
        this.parser = parser;
        this.root = root;
    }

    @NotNull
    public Scope addChild(@NotNull String source, @NotNull String name) {
        checkDestroyed();

        return new ScriptScope(this, file, source, name, false);
    }

    @NotNull
    @Override
    public var addErrorHandler(@NotNull var handler) {
        checkDestroyed();

        errorHandlers.add(handler);
        return $void();

    }

    @Override
    public void clear() {
        checkDestroyed();

        if (getConfig().debugScope()) {
            log.info("Clearing scope {}", this);
        }
        variables.clear();
        listeners.clear();
    }

    @NotNull
    @Override
    public var get(@NotNull String key, boolean mustFind) {
        checkDestroyed();
        if (key.matches("[0-9]+")) {
            throw new DollarAssertionException("Cannot get numerical keys, use getParameter");
        }
        if (getConfig().debugScope()) {
            log.info("Looking up {} in {}", key, this);
        }
        Scope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        } else {
            if (getConfig().debugScope()) {
                log.info("{} in {}", DollarScriptSupport.ansiColor("FOUND " + key, DollarScriptSupport.ANSI_CYAN), scope);
            }
        }
        Variable result = (Variable) scope.getVariables().get(key);

        if (mustFind) {
            if (result == null) {
                throw new VariableNotFoundException(key, this);
            } else {
                return result.getValue();
            }
        } else {
            return (result != null) ? result.getValue() : $void();
        }
    }

    @Override
    public var get(@NotNull String key) {
        return get(key, false);
    }

    @Nullable
    @Override
    public var getConstraint(@NotNull String key) {
        checkDestroyed();

        Scope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }
        if (getConfig().debugScope()) {
            log.info("Getting constraint for {} in {}", key, scope);
        }
        if (scope.getVariables().containsKey(key) && (((Variable) scope.getVariables().get(
                key)).getConstraint() != null)) {
            return ((Variable) scope.getVariables().get(key)).getConstraint();
        }
        return null;
    }

    @Nullable
    @Override
    public String getConstraintSource(@NotNull String key) {
        checkDestroyed();

        Scope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }
        if (getConfig().debugScope()) {
            log.info("Getting constraint for {} in {}", key, scope);
        }
        if (scope.getVariables().containsKey(key) && (((Variable) scope.getVariables().get(
                key)).getConstraintSource() != null)) {
            return ((Variable) scope.getVariables().get(key)).getConstraintSource();
        }
        return null;
    }

    @Override
    public String getFile() {
        return file;
    }

    @NotNull
    @Override
    public MultiMap<String, Listener> getListeners() {
        return listeners;
    }

    @NotNull
    @Override
    public var getParameter(@NotNull String key) {
        checkDestroyed();

        if (getConfig().debugScope()) {
            log.info("Looking up parameter {} in {}", key, this);
        }
        Scope scope = getScopeForParameters();
        if (scope == null) {
            scope = this;
        } else {
            if (getConfig().debugScope()) {
                log.info("Found {} in {}", key, scope);
            }
        }
        Variable result = ((Variable) scope.getVariables().get(key));

        return (result != null) ? result.getValue() : $void();
    }

    @Nullable
    @Override
    public Scope getScopeForKey(@NotNull String key) {
        checkDestroyed();

        if (variables.containsKey(key)) {
            return this;
        }
        if (parent != null) {
            return parent.getScopeForKey(key);
        } else {
//            if (DollarStatic.getConfig().debugScope()) { log.info("Scope not found for " + key); }
            return null;
        }
    }

    @Nullable
    @Override
    public Scope getScopeForParameters() {
        checkDestroyed();

        if (parameterScope) {
            return this;
        }
        if (parent != null) {
            return parent.getScopeForParameters();
        } else {
            if (getConfig().debugScope()) {
                log.info("Parameter scope not found.");
            }
            return null;
        }
    }

    @Nullable
    @Override
    public String getSource() {
        return source;
    }

    @NotNull
    @Override
    public Map<String, Variable> getVariables() {
        return variables;
    }

    @NotNull
    @Override
    public var handleError(@NotNull Throwable t) {
        log.warn(t.getMessage(), t);
        if (errorHandlers.isEmpty()) {
            if (parent == null) {
                if (t instanceof ParserException) {
                    throw (ParserException) t;
                }
                if (t instanceof DollarParserError) {
                    throw (DollarParserError) t;
                }
                if (t instanceof DollarException) {
                    throw (DollarException) t;
                }
                throw new DollarScriptException(t);
            } else {
                return parent.handleError(t);
            }
        } else {
            setParameter("type", $(t.getClass().getName()));
            setParameter("msg", $(t.getMessage()));
            try {
                for (var handler : errorHandlers) {
                    fix(handler, false);
                }
            } finally {
                setParameter("type", $void());
                setParameter("msg", $void());
            }
            return $void();
        }

    }

    @Override
    public boolean has(@NotNull String key) {
        checkDestroyed();

        Scope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }
        if (getConfig().debugScope()) {
            log.info("Checking for {} in {}", key, scope);
        }

        Variable val = ((Variable) scope.getVariables().get(key));
        return val != null;

    }

    @Override
    public boolean hasParameter(@NotNull String key) {
        checkDestroyed();

        if (getConfig().debugScope()) {
            log.info("Looking up parameter {} in {}", key, this);
        }
        Scope scope = getScopeForParameters();
        if (scope == null) {
            scope = this;
        } else {
            if (getConfig().debugScope()) {
                log.info("Found {} in {}", key, scope);
            }
        }
        Variable result = ((Variable) scope.getVariables().get(key));

        return result != null;
    }

    @Override
    public void listen(@NotNull String key, @NotNull String id, @NotNull var listener) {
        checkDestroyed();
        listen(key, id, in -> {
            log.debug("Notifying {}", listener.source().getSourceMessage());
                   listener.$notify();
                   return $void();
               }
        );

    }

    @Override
    public void listen(@NotNull String key, @NotNull String id, @NotNull Pipeable pipe) {
        checkDestroyed();
        Listener listener = new Listener() {
            @NotNull
            @Override
            public String getId() {
                return id;
            }

            @NotNull
            @Override
            public var pipe(var... vars) throws Exception {
                return pipe.pipe(vars);
            }
        };

        if (key.matches("[0-9]+")) {
            if (getConfig().debugScope()) {
                log.info("Cannot listen to positional parameter ${} in {}", key, this);
            }
            return;
        }
        Scope scopeForKey = getScopeForKey(key);
        if (scopeForKey == null) {
            if (getConfig().debugScope()) {
                log.info("Key {} not found in {}", key, this);
            }
            scopeForKey = this;
        }

        if (getConfig().debugScope()) {
            log.info("Listening for {} in {}", key, scopeForKey);
        }


        if (listeners.getCollection(key).stream().filter(i -> i.getId().equals(id)).count() == 0) {
            scopeForKey.getListeners().putValue(key, listener);
        }
        log.debug("Listener size now {}", scopeForKey.getListeners().getCollection(key).size());
    }

    @Nullable
    @Override
    public var notify(@NotNull String variableName) {
        checkDestroyed();
        var value = get(variableName);
        notifyScope((variableName), value);
        return value;
    }

    @Override
    public void notifyScope(@NotNull String key, @NotNull var value) {
        checkDestroyed();

        if (value == null) {
            throw new NullPointerException();
        }
        log.debug("Scope {} notified for {}", this, key);
        if (listeners.containsKey(key)) {
            log.debug("Scope {} notified for {} with {} listeners", this, key, listeners.getCollection(
                    key)
                                                                                       .size());
            new ArrayList<>(listeners.getCollection(key)).forEach(
                    pipe -> {
                        try {
                            pipe.pipe($(key), value);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            throw new DollarScriptException(e);
                        }
                    });
        } else {
            log.debug("Scope {} notified for {} NO LISTENERS", this, key);
        }
    }

    @NotNull
    @Override
    public var set(@NotNull String key,
                   @NotNull var value,
                   boolean readonly,
                   @Nullable var constraint,
                   String constraintSource,
                   boolean isVolatile,
                   boolean fixed,
                   boolean pure) {
        checkDestroyed();

        if (key.matches("[0-9]+")) {
            throw new DollarAssertionException("Cannot set numerical keys, use setParameter");
        }
        Scope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }

        if (scope.getVariables().containsKey(key) && ((Variable) scope.getVariables().get(
                key)).isReadonly()) {
            throw new DollarScriptException("Cannot change the value of variable " + key + " it is readonly");
        }
        if (scope.getVariables().containsKey(key)) {
            final Variable variable = ((Variable) scope.getVariables().get(key));
            if (!variable.isVolatile() && (variable.getThread() != Thread.currentThread().getId())) {
                handleError(
                        new DollarScriptException("Concurrency Error: Cannot change the variable " +
                                                          key +
                                                          " in a different thread from that which is created in."));
            }
            if (variable.getConstraint() != null) {
                if (constraint != null) {
                    handleError(new DollarScriptException("Cannot change the constraint on a variable, attempted to redeclare for " + key));
                }
            }
            if (getConfig().debugScope()) {
                log.info("Setting {} in {}", key, scope);
            }
            variable.setValue(value);
        } else {
            if (getConfig().debugScope()) {
                log.info("Adding {} in {}", key, scope);
            }
            scope.getVariables().put(key, new Variable(value, readonly, constraint, constraintSource, isVolatile, fixed, pure));
        }
        scope.notifyScope(key, value);
        return value;
    }

    @NotNull
    @Override
    public var setParameter(@NotNull String key, @NotNull var value) {
        checkDestroyed();

        if (getConfig().debugScope()) {
            log.info("Setting parameter {} in {}", key, this);
        }
        if (key.matches("[0-9]+") && variables.containsKey(key)) {
            throw new DollarScriptException("Cannot change the value of positional variable $" + key + " in scope " + this);
        }
        parameterScope = true;
        variables.put(key, new Variable(value, null, null));
        notifyScope(key, value);
        return value;
    }

    private void checkDestroyed() {
        if (destroyed) {
            throw new IllegalStateException("Attempted to use a destroyed scope " + this);
        }
    }

    @NotNull
    public Parser<var> getParser() {
        return parser;
    }

    public void setParser(@NotNull Parser<var> parser) {
        this.parser = parser;
    }

    @Nullable
    @Override
    public String toString() {
        return id + "->" + parent;
    }

    private boolean checkConstraint(@NotNull var value,
                                    @Nullable Variable oldValue,
                                    @NotNull var constraint) {
        checkDestroyed();

        setParameter("it", value);
        log.debug("SET it={}", value);
        if (oldValue != null) {
            setParameter("previous", oldValue.getValue());
        }
        final boolean fail = constraint.isFalse();
        setParameter("it", $void());
        setParameter("previous", $void());
        return fail;
    }    @Override
    public Scope getParent() {
        return parent;
    }




    @Override
    public void setParent(@Nullable Scope scope) {
        checkDestroyed();

        if (isRoot()) {
            throw new UnsupportedOperationException("Cannot set the parent scope of a root scope, attempted to set " + scope);
        }
        parent = scope;
        source = scope.getSource();
        file = scope.getFile();

    }


    @Override
    public boolean hasParent(Scope scope) {
        checkDestroyed();

        if (getParent() == null) {
            return false;
        }
        return getParent().equals(scope) || getParent().hasParent(scope);
    }


    @Override
    public boolean isRoot() {
        return root;
    }

    @NotNull
    @Override
    public Scope copy() {
        checkDestroyed();

        return new ScriptScope(parent, "*" + id.split(":")[0] + ":" + counter.incrementAndGet(), file,
                               parameterScope,
                               variables, errorHandlers, listeners, source, parser, root);
    }

    @Override
    public void destroy() {
        clear();
        destroyed = true;
    }

    @Override
    public boolean pure() {
        return false;
    }


}
