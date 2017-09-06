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

import dollar.api.DollarStatic;
import dollar.api.Scope;
import dollar.api.Variable;
import dollar.api.var;
import dollar.internal.runtime.script.api.exceptions.DollarScriptException;
import dollar.internal.runtime.script.api.exceptions.VariableNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static dollar.api.DollarStatic.$void;

public class PureScope extends ScriptScope {
    @NotNull
    private static final Logger log = LoggerFactory.getLogger(PureScope.class);

    PureScope(@NotNull Scope parent, @NotNull String source, @NotNull String name, @Nullable String file, boolean parallel) {
        super(parent, _file(parent, file), source, name, false, parallel, false);
    }

    @NotNull
    private static String _file(@NotNull Scope parent, @Nullable String file) {
        if (file != null) {
            return file;
        } else {
            String parentFile = parent.file();
            assert parentFile != null;
            return parentFile;
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Cannot clear a pure scope");
    }

    @NotNull
    @Override
    public var get(@NotNull String key, boolean mustFind) {
        if (key.matches("[0-9]+")) {
            throw new AssertionError("Cannot get numerical keys, use parameter");
        }
        if (DollarStatic.getConfig().debugScope()) {
            log.info("Looking up {} in {}", key, this);
        }
        Scope scope = scopeForKey(key);
        if (scope == null) {
            scope = this;
        } else {
            if (DollarStatic.getConfig().debugScope()) {
                log.info("Found {} in {}", key, scope);
            }
        }
        Variable result = (Variable) scope.variables().get(key);
        if ((result != null)) {
            if (!result.isPure()) {
                throw new DollarScriptException("Cannot access impure values in a pure expression, put  'pure'  before the " +
                                                        "definition of '" + key + "' (" + this + ")");
            }
            if (result.isReadonly()) {

            } else {
                throw new DollarScriptException("Cannot access non constant values in a pure expression, put 'const' before  the " +
                                                        "definition of '" + key + "' (" + this + ")");
            }
        }
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
    public var set(@NotNull String key, @NotNull var value, boolean readonly, @Nullable var constraint,
                   @Nullable String constraintSource,
                   boolean isVolatile, boolean fixed,
                   boolean pure) {
        if (!pure) {
            throw new DollarScriptException("Cannot have impure variables in a pure expression, variable was " + key + ", (" + this + ")",
                                            value);
        }
        if (isVolatile) {
            throw new DollarScriptException("Cannot have volatile variables in a pure expression");
        }
        if (key.matches("[0-9]+")) {
            throw new AssertionError("Cannot set numerical keys, use parameter");
        }
        Scope scope = scopeForKey(key);
        if ((scope != null) && !Objects.equals(scope, this)) {
            throw new DollarScriptException("Cannot modify variables outside of a pure scope");
        }
        if (scope == null) {
            scope = this;
        }
        if (DollarStatic.getConfig().debugScope()) { log.info("Setting {} in {}", key, scope); }
        if (scope.variables().containsKey(key) && ((Variable) scope.variables().get(key)).isReadonly()) {
            throw new DollarScriptException("Cannot change the value of variable " + key + " it is readonly");
        }
        final var fixedValue = fixed ? value.$fixDeep() : value;
        if (scope.variables().containsKey(key)) {
            final Variable variable = ((Variable) scope.variables().get(key));
            if (!variable.isVolatile() && (variable.getThread() != Thread.currentThread().getId())) {
                handleError(new DollarScriptException("Concurrency Error: Cannot change the variable " +
                                                              key +
                                                              " in a different thread from that which is created in."));
            }
            if (variable.getConstraint() != null) {
                handleError(
                        new DollarScriptException("Cannot change the constraint on a variable, attempted to redeclare for " + key));
            }
            variable.setValue(fixedValue);
        } else {
            scope.variables()
                    .put(key, new Variable(fixedValue, readonly, constraint, constraintSource, false, fixed, pure, false,
                                           false));
        }
        scope.notifyScope(key, fixedValue);
        return value;
    }

    @Override
    public boolean pure() {
        return true;
    }

    @NotNull
    @Override
    public String toString() {
        return id + "(P)" + (parallel() ? "=>" : "->") + parent;
    }


}
