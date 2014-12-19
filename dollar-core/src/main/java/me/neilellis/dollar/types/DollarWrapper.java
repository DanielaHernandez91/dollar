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

package me.neilellis.dollar.types;

import com.github.oxo42.stateless4j.StateMachine;
import me.neilellis.dollar.*;
import me.neilellis.dollar.collections.ImmutableList;
import me.neilellis.dollar.collections.ImmutableMap;
import me.neilellis.dollar.json.ImmutableJsonObject;
import me.neilellis.dollar.monitor.DollarMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The DollarWrapper class exists to provide basic AOP style constructs without having actual AOP magic.
 *
 * Currently the DollarWrapper class provides monitoring and state tracing functions.
 *
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public class DollarWrapper implements var {

    private final DollarMonitor monitor;
    private final StateTracer tracer;
    private final ErrorLogger errorLogger;
    private final var value;

    DollarWrapper(var value, DollarMonitor monitor, StateTracer tracer, ErrorLogger errorLogger) {
//        tracer.trace(DollarNull.INSTANCE,value, StateTracer.Operations.CREATE);
        this.value = value;
        this.monitor = monitor;
        this.tracer = tracer;
        this.errorLogger = errorLogger;
        if (value == null) {
            throw new NullPointerException();
        }
    }

    @NotNull
    @Override
    public var $abs() {
        return getValue().$abs();
    }

    @NotNull
    @Override
    public var $minus(@NotNull var v) {
        return getValue().$minus(v);
    }

    @NotNull
    @Override
    public var $plus(var v) {
        return getValue().$plus(v);
    }

    @NotNull
    @Override
    public var $negate() {
        return getValue().$negate();
    }

    @NotNull
    @Override
    public var $divide(@NotNull var v) {
        return getValue().$divide(v);
    }

    @NotNull
    @Override
    public var $modulus(@NotNull var v) {
        return getValue().$modulus(v);
    }

    @NotNull
    @Override
    public var $multiply(@NotNull var v) {
        return getValue().$multiply(v);
    }

    @NotNull @Override
    public Integer I() {
        return getValue().I();
    }

    @NotNull @Override
    public Long L() {
        return getValue().L();
    }

    @NotNull
    @Override
    public Number N() {
        return getValue().N();
    }

    @Override public int sign() {
        return getValue().sign();
    }

    @NotNull @Override
    public Double D() {
        return getValue().D();
    }

    var getValue() {
        if (value == null) {
            throw new IllegalStateException("Value has become null!!");
        }
        return value;
    }

    @Override
    public var $all() {
        return getValue().$all();
    }

    @Override
    public var $dispatch(var lhs) {
        return getValue().$dispatch(lhs);

    }

    @Override
    public var $write(var value, boolean blocking, boolean mutating) {
        return getValue().$write(value, blocking, mutating);
    }

    @Override
    public var $drain() {
        return getValue().$drain();
    }

    @Override
    public var $give(var lhs) {
        return getValue().$give(lhs);

    }

    @Override
    public var $listen(Pipeable pipe) {
        return getValue().$listen(pipe);
    }

    @Override
    public var $notify() {
        return getValue().$notify();
    }

    @Override
    public var $peek() {
        return getValue().$peek();

    }

    @Override
    public var $read(boolean blocking, boolean mutating) {
        return getValue().$read(blocking, mutating);
    }

    @Override
    public var $poll() {
        return getValue().$poll();
    }

    @NotNull
    @Override
    public var $pop() {
        return tracer.trace(DollarVoid.INSTANCE,
                            monitor.run("pop",
                                        "dollar.persist.temp.pop",
                                        "Popping value from ",
                                        () -> getValue().$pop()),
                            StateTracer.Operations.POP);
    }

    @Override
    public var $publish(var lhs) {
        return getValue().$publish(lhs);

    }

    @Override
    public var $push(var lhs) {
        return getValue().$push(lhs);

    }

    @Override
    public var $read() {
        return getValue().$read();
    }

    @Override
    public var $subscribe(Pipeable subscription) {
        return getValue().$subscribe(subscription);
    }

    @Override
    public var $listen(Pipeable pipe, String key) {
        return getValue().$listen(pipe, key);
    }

    @Override public var $subscribe(Pipeable subscription, String key) {
        return getValue().$subscribe(subscription, key);
    }

    @Override
    public var $write(var value) {
        return getValue().$write(value);
    }

    @Override
    public var $choose(var map) {
        return tracer.trace(this, getValue().$choose(map), StateTracer.Operations.CHOOSE);
    }

    @Override
    public var $each(Pipeable pipe) {
        return getValue().$each(pipe);
    }

    @NotNull
    @Override
    public var $copy() {
        return getValue().$copy();
    }

    @NotNull @Override public var _fix(boolean parallel) {
        return getValue()._fix(parallel);
    }

    @Override public var _fix(int depth, boolean parallel) {
        return getValue()._fix(depth, parallel);
    }

    @Override public var _fixDeep(boolean parallel) {
        return getValue()._fixDeep(parallel);
    }

    @Override
    public void _src(String src) {
        getValue()._src(src);
    }

    @Override
    public String _src() {
        return getValue()._src();
    }

    @NotNull
    @Override
    public var _unwrap() {
        return getValue()._unwrap();
    }

    @NotNull
    @Override
    public var copy(@NotNull ImmutableList<Throwable> errors) {
        return getValue().$copy();
    }

    @NotNull @Override public var $create() {
        return getValue().$create();
    }

    @NotNull @Override public var $destroy() {
        return getValue().$destroy();
    }

    @NotNull @Override public var $pause() {
        return getValue().$pause();
    }

    @Override public void $signal(@NotNull Signal signal) {
        getValue().$signal(signal);
    }

    @NotNull @Override public var $start() {
        return getValue().$start();
    }

    @NotNull @Override public var $state() {
        return getValue().$state();
    }

    @NotNull @Override public var $stop() {
        return getValue().$stop();
    }

    @NotNull @Override public var $unpause() {
        return getValue().$unpause();
    }

    @NotNull @Override public StateMachine<ResourceState, Signal> getStateMachine() {
        return getValue().getStateMachine();
    }

    @NotNull
    @Override
    public var $default(var v) {
        return getValue().$default(v);
    }

    @NotNull
    @Override
    public var $mimeType() {
        return getValue().$mimeType();
    }

    @NotNull
    @Override
    public Stream<var> $stream(boolean parallel) {
        return getValue().$stream(false);
    }

    @NotNull
    @Override
    public var err() {
        return getValue().err();

    }

    @NotNull
    @Override
    public var out() {
        return getValue().out();
    }

    @NotNull @Override public String toDollarScript() {
        return getValue().toString();
    }

    @Nullable
    @Override
    public <R> R toJavaObject() {
        return getValue().toJavaObject();
    }

    @Nullable
    @Override
    public JSONObject toOrgJson() {
        return getValue().toOrgJson();
    }

    @Nullable
    @Override
    public ImmutableJsonObject toJsonObject() {
        return getValue().toJsonObject();
    }

    @NotNull
    @Override
    public var $error(@NotNull String errorMessage) {
        errorLogger.log(errorMessage);
        return getValue().$error(errorMessage);
    }

    @NotNull
    @Override
    public var $error(@NotNull Throwable error) {
        errorLogger.log(error);
        return getValue().$error(error);
    }

    @NotNull
    @Override
    public var $error() {
        errorLogger.log();
        return getValue().$error();
    }

    @NotNull
    @Override
    public var $errors() {
        return getValue().$errors();
    }

    @NotNull
    @Override
    public var $fail(@NotNull Consumer<ImmutableList<Throwable>> handler) {
        return getValue().$fail(handler);
    }

    @NotNull
    @Override
    public var $invalid(@NotNull String errorMessage) {
        errorLogger.log();
        return getValue().$invalid(errorMessage);
    }

    @NotNull
    @Override
    public var $error(@NotNull String errorMessage, @NotNull ErrorType type) {
        errorLogger.log(errorMessage, type);
        return getValue().$error(errorMessage, type);
    }

    @NotNull @Override
    public var clearErrors() {
        return tracer.trace(this, getValue().clearErrors(), StateTracer.Operations.CLEAR_ERRORS);
    }

    @NotNull
    @Override
    public List<String> errorTexts() {
        return getValue().errorTexts();
    }

    @NotNull
    @Override
    public ImmutableList<Throwable> errors() {
        return getValue().errors();
    }

    @Override
    public boolean hasErrors() {
        return getValue().hasErrors();
    }

    @NotNull
    @Override
    public var $eval(@NotNull String js) {
        return $pipe("anon", js);
    }

    @NotNull
    private static String sanitize(@NotNull String location) {
        return location.replaceAll("[^\\w.]+", "_");

    }

    @NotNull
    @Override
    public var $pipe(@NotNull String label, @NotNull Pipeable pipe) {
        return tracer.trace(this,
                            monitor.run("$pipe",
                                        "dollar.pipe.pipeable." + sanitize(label), "",
                                        () -> getValue().$pipe(label, pipe)),
                            StateTracer.Operations.EVAL, label,
                            pipe.getClass().getName());
    }

    @NotNull
    @Override
    public var $pipe(@NotNull String label, @NotNull String js) {
        return tracer.trace(this,
                            monitor.run("$pipe",
                                        "dollar.pipe.js." + sanitize(label),
                                        "Evaluating: " + js,
                                        () -> getValue().$pipe(label, js)),
                            StateTracer.Operations.EVAL,
                            label,
                            js);
    }

    @NotNull
    @Override
    public var $pipe(@NotNull Class<? extends Pipeable> clazz) {
        return tracer.trace(this,
                            monitor.run("pipe",
                                        "dollar.run.pipe." + sanitize(clazz),
                                        "Piping to " + clazz.getName(),
                                        () -> getValue().$pipe(clazz)),
                            StateTracer.Operations.PIPE,
                            clazz.getName());
    }

    @NotNull
    private String sanitize(@NotNull Class clazz) {
        return clazz.getName().toLowerCase();
    }

    @NotNull
    @Override
    public var $get(@NotNull var rhs) {
        return getValue().$get(rhs);
    }

    @NotNull @Override public var $append(@NotNull var value) {
        return getValue().$append(value);
    }

    @NotNull
    @Override
    public var $containsValue(@NotNull var value) {
        return getValue().$containsValue(value);
    }

    @NotNull
    @Override
    public var $has(@NotNull var key) {
        return DollarStatic.$(getValue().$has(key));
    }

    @NotNull
    @Override
    public var $isEmpty() {
        return DollarStatic.$(getValue().$isEmpty());
    }

    @NotNull
    @Override
    public var $size() {
        return DollarStatic.$(getValue().$size());
    }

    @NotNull @Override public var $prepend(@NotNull var value) {
        return getValue().$prepend(value);
    }

    @NotNull
    @Override
    public var $removeByKey(@NotNull String key) {
        return tracer.trace(this, getValue().$removeByKey(key), StateTracer.Operations.REMOVE_BY_KEY, key);
    }

    @NotNull
    @Override
    public var $set(@NotNull String key, Object value) {
        return tracer.trace(null, getValue().$set(key, value), StateTracer.Operations.SET, key, value);
    }

    @NotNull
    @Override
    public var $set(@NotNull var key, Object value) {
        return tracer.trace(this, getValue().$set(key, value), StateTracer.Operations.SET, key, value);
    }

    @NotNull
    @Override
    public var remove(Object value) {
        return tracer.trace(this, getValue().remove(value), StateTracer.Operations.REMOVE_BY_VALUE, value);
    }

    @NotNull @Override public var $remove(var value) {
        return getValue().$remove(value);
    }

    @Override
    public String S() {
        return getValue().S();
    }

    @Override
    public var $as(Type type) {
        return getValue().$as(type);
    }

    @NotNull
    @Override
    public ImmutableList<var> $list() {
        return getValue().$list();
    }

    @Override public Type $type() {
        return getValue().$type();
    }

    @NotNull
    @Override
    public ImmutableMap<var, var> $map() {
        return getValue().$map();
    }

    @Override
    public boolean is(@NotNull Type... types) {
        return getValue().is(types);
    }

    @Override public boolean isCollection() {
        return getValue().isCollection();
    }

    @Override
    public boolean isDecimal() {
        return getValue().isDecimal();
    }

    @Override
    public boolean isInteger() {
        return getValue().isInteger();
    }

    @Override
    public boolean isLambda() {
        return getValue().isLambda();
    }

    @Override
    public boolean isList() {
        return getValue().isList();
    }

    @Override
    public boolean isMap() {
        return getValue().isMap();
    }

    @Override
    public boolean isNumber() {
        return getValue().isNumber();
    }

    @Override public boolean isPair() {
        return getValue().isPair();
    }

    @Override
    public boolean isSingleValue() {
        return getValue().isSingleValue();
    }

    @Override
    public boolean isString() {
        return getValue().isString();
    }

    @Override
    public boolean isUri() {
        return getValue().isUri();
    }

    @Override
    public boolean isVoid() {
        return getValue().isVoid();
    }

    @Override
    public ImmutableList<String> strings() {
        return getValue().strings();
    }

    @NotNull @Override public ImmutableList<?> toList() {
        return getValue().toList();
    }

    @NotNull @Override
    public Map<?, ?> toMap() {
        return getValue().toMap();
    }

    @NotNull @Override
    public InputStream toStream() {
        return getValue().toStream();
    }

    @Override
    public var assertFalse(Function<var, Boolean> assertion, String message) throws AssertionError {
        return getValue().assertFalse(assertion, message);
    }

    @Override
    public var assertNotVoid(String message) throws AssertionError {
        return getValue().assertNotVoid(message);
    }

    @Override
    public var assertTrue(Function<var, Boolean> assertion, String message) throws AssertionError {
        return getValue().assertTrue(assertion, message);
    }

    @Override
    public int compareTo(@NotNull var o) {
        return getValue().compareTo(o);
    }

    @Override
    public var debug(Object message) {
        return getValue().debug(message);
    }

    @Override
    public var debug() {
        return getValue().debug();
    }

    @Override
    public var debugf(String message, Object... values) {
        return getValue().debugf(message, values);
    }

    @Override
    public var error(Throwable exception) {
        return getValue().error(exception);
    }

    @Override
    public var error(Object message) {
        return getValue().error(message);
    }

    @Override
    public var error() {
        return getValue().error();
    }

    @Override
    public var errorf(String message, Object... values) {
        return getValue().errorf(message, values);
    }

    @Override
    public var info(Object message) {
        return getValue().info(message);
    }

    @Override
    public var info() {
        return getValue().info();
    }

    @Override
    public var infof(String message, Object... values) {
        return getValue().infof(message, values);
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") @Override
    public boolean equals(Object obj) {
        return getValue().equals(obj);
    }

    @NotNull
    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public boolean isBoolean() {
        return getValue().isBoolean();
    }

    @Override
    public boolean isFalse() {
        return getValue().isFalse();
    }

    @Override
    public boolean isNeitherTrueNorFalse() {
        return getValue().isNeitherTrueNorFalse();
    }

    @Override
    public boolean isTrue() {
        return getValue().isTrue();
    }

    @Override
    public boolean isTruthy() {
        return getValue().isTruthy();
    }

    @Override
    public void setMetaAttribute(String key, String value) {
        getValue().setMetaAttribute(key, value);
    }

    @Override
    public String getMetaAttribute(String key) {
        return getValue().getMetaAttribute(key);
    }

}
