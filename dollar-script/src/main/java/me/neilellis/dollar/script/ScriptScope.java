/*
 * Copyright (c) 2014 Neil Ellis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.neilellis.dollar.script;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import me.neilellis.dollar.DollarStatic;
import me.neilellis.dollar.script.exceptions.DollarScriptException;
import me.neilellis.dollar.var;
import org.codehaus.jparsec.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static me.neilellis.dollar.DollarStatic.*;

/**
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public class ScriptScope implements Scope {
    private static final Logger log = LoggerFactory.getLogger(ScriptScope.class);

    private static AtomicInteger counter = new AtomicInteger();
    private final String id;
    private final String source;
    private ScriptScope parent;
    private ConcurrentHashMap<String, Variable> variables = new ConcurrentHashMap<>();
    private boolean lambdaUnderConstruction;
    private Parser<var> parser;
    private DollarParser dollarParser;
    private Multimap<String, var> listeners = LinkedListMultimap.create();
    private boolean parameterScope;
    private List<var> errorHandlers = new CopyOnWriteArrayList<>();

    public ScriptScope(String name) {
        this.parent = null;
        this.source = "<unknown>";
        this.dollarParser = null;
        id = String.valueOf(name + ":" + counter.incrementAndGet());
    }

    public ScriptScope(ScriptScope parent, String source) {
        this.parent = parent;
        this.source = source;
        this.dollarParser = parent.getDollarParser();
        id = String.valueOf("S:" + counter.incrementAndGet());
    }

    public DollarParser getDollarParser() {
        return dollarParser;
    }

    public void setDollarParser(DollarParser dollarParser) {
        this.dollarParser = dollarParser;
    }

    public ScriptScope(DollarParser dollarParser, String source) {
        this.source = source;
        this.dollarParser = dollarParser;
        id = String.valueOf("S:" + counter.incrementAndGet());
    }

    public ScriptScope addChild(String source) {
        return new ScriptScope(this, source);
    }

    public void clear() {
        if (DollarStatic.config.isDebugScope()) { log.info("Clearing scope " + this); }
        variables.clear();
        listeners.clear();
    }

    @Override
    public var get(String key) {
        if (key.matches("[0-9]+")) {
            throw new AssertionError("Cannot get numerical keys, use getParameter");
        }
        if (DollarStatic.config.isDebugScope()) { log.info("Looking up " + key + " in " + this); }
        ScriptScope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        } else {
            if (DollarStatic.config.isDebugScope()) { log.info("Found " + key + " in " + scope); }
        }
        Variable result = scope.variables.get(key);

        return result != null ? result.value : $void();
    }

    @Override
    public var getParameter(String key) {
        if (DollarStatic.config.isDebugScope()) { log.info("Looking up parameter " + key + " in " + this); }
        ScriptScope scope = getScopeForParameters();
        if (scope == null) {
            scope = this;
        } else {
            if (DollarStatic.config.isDebugScope()) { log.info("Found " + key + " in " + scope); }
        }
        Variable result = scope.variables.get(key);

        return result != null ? result.value : $void();
    }

    @Override
    public boolean hasParameter(String key) {
        if (DollarStatic.config.isDebugScope()) { log.info("Looking up parameter " + key + " in " + this); }
        ScriptScope scope = getScopeForParameters();
        if (scope == null) {
            scope = this;
        } else {
            if (DollarStatic.config.isDebugScope()) { log.info("Found " + key + " in " + scope); }
        }
        Variable result = scope.variables.get(key);

        return result != null;
    }

    @Override
    public boolean has(String key) {
        ScriptScope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }
        if (DollarStatic.config.isDebugScope()) {
            log.info("Checking for " + key + " in " + scope);
        }

        Variable val = scope.variables.get(key);
        return val != null;

    }

    @Override
    public var set(String key, var value, boolean readonly, var constraint) {
        if (key.matches("[0-9]+")) {
            throw new AssertionError("Cannot set numerical keys, use setParameter");
        }
        ScriptScope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }
        if (DollarStatic.config.isDebugScope()) { log.info("Setting " + key + " in " + scope); }
        if (scope.variables.containsKey(key) && scope.variables.get(key).readonly) {
            throw new DollarScriptException("Cannot change the value of variable " + key + " it is readonly");
        }
        if (scope.variables.containsKey(key) && scope.variables.get(key).constraint != null) {
            if (constraint != null) {
                handleError(new DollarScriptException(
                        "Cannot change the constraint on a variable, attempted to redeclare for " + key));
            }
//            if (scope.checkConstraint(value, scope.variables.get(key), scope.variables.get(key).constraint)) {
//                handleError(new DollarScriptException("Constraint failed for variable " + key + ""));
//            }
        }
//        if (constraint != null && scope.checkConstraint(value, scope.variables.get(key), constraint)) {
//            handleError(new DollarScriptException("Constraint failed for variable " + key + ""));
//        }
        if (scope.variables.containsKey(key)) {
            scope.variables.get(key).value = value;
        } else {
            scope.variables.put(key, new Variable(value, readonly, constraint));
        }
        scope.notifyScope(key, value);
        return value;
    }

    @Override
    public void notifyScope(String key, var value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (listeners.containsKey(key)) {
            for (var listener : listeners.get(key)) {
                listener.$notify(value);
            }
        }
    }

    @Override
    public void listen(String key, var listener) {
        if (key.matches("[0-9]+")) {
            if (DollarStatic.config.isDebugScope()) {
                log.info("Cannot listen to positional parameter $" + key + " in " + this);
            }
            return;
        }
        ScriptScope scopeForKey = getScopeForKey(key);
        if (scopeForKey == null) {
            if (DollarStatic.config.isDebugScope()) { log.info("Key " + key + " not found in " + this); }
            listeners.put(key, listener);
            return;
        }
        if (DollarStatic.config.isDebugScope()) { log.info("Listening for " + key + " in " + scopeForKey); }
        scopeForKey.listeners.put(key, listener);
    }

    @Override
    public var addErrorHandler(var handler) {
        errorHandlers.add(handler);
        return $void();
    }

    public var getConstraint(String key) {
        ScriptScope scope = getScopeForKey(key);
        if (scope == null) {
            scope = this;
        }
        if (DollarStatic.config.isDebugScope()) { log.info("Getting constraint for " + key + " in " + scope); }
        if (scope.variables.containsKey(key) && scope.variables.get(key).constraint != null) {
            return scope.variables.get(key).constraint;
        }
        return null;
    }

    public Parser<var> getParser() {
        return parser;
    }

    public void setParser(Parser<var> parser) {
        this.parser = parser;
    }

    public String getSource() {
        return source;
    }

    public var handleError(Throwable t) {
        if (errorHandlers.isEmpty()) {
            if (parent != null) {
                return parent.handleError(t);
            } else {
                throw new DollarScriptException(t);
            }
        } else {
            setParameter("type", $(t.getClass().getName()));
            setParameter("msg", $(t.getMessage()));
            try {
                for (var handler : errorHandlers) {
                    fix(handler);
                }
            } finally {
                setParameter("type", $void());
                setParameter("msg", $void());
            }
            return $void();
        }

    }

    public void setParent(ScriptScope scriptScope) {
        this.parent = scriptScope;
    }

    private class Variable {
        private final boolean readonly;
        private final var constraint;
        private var value;

        public Variable(var value, var constraint) {

            this.value = value;
            this.constraint = constraint;
            readonly = false;
        }

        public Variable(var value, boolean readonly, var constraint) {
            this.value = value;
            this.readonly = readonly;
            this.constraint = constraint;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }

            Variable variable = (Variable) o;

            if (!value.equals(variable.value)) { return false; }

            return true;
        }
    }

    private boolean checkConstraint(var value, Variable oldValue, var constraint) {
        setParameter("it", value);
        System.out.println("SET it=" + value);
        if (oldValue != null) {
            setParameter("previous", oldValue.value);
        }
        final boolean fail = constraint.isFalse();
        setParameter("it", $void());
        setParameter("previous", $void());
        return fail;
    }

    public var setParameter(String key, var value) {
        if (DollarStatic.config.isDebugScope()) { log.info("Setting parameter " + key + " in " + this); }
        if (key.matches("[0-9]+") && variables.containsKey(key)) {
            throw new AssertionError("Cannot change the value of positional variables.");
        }
        this.parameterScope = true;
        variables.put(key, new Variable(value, null));
        this.notifyScope(key, value);
        return value;
    }

    private ScriptScope getScopeForKey(String key) {
        if (variables.containsKey(key)) {
            return this;
        }
        if (parent != null) {
            return parent.getScopeForKey(key);
        } else {
            if (DollarStatic.config.isDebugScope()) { log.info("Scope not found for " + key); }
            return null;
        }
    }

    @Override
    public String toString() {
        return id + "->" + parent;
    }

    private ScriptScope getScopeForParameters() {
        if (parameterScope) {
            return this;
        }
        if (parent != null) {
            return parent.getScopeForParameters();
        } else {
            if (DollarStatic.config.isDebugScope()) { log.info("Parameter scope not found."); }
            return null;
        }
    }


}
