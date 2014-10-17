package me.neilellis.dollar;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.vertx.java.core.json.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public class DollarFuture implements var {

    private Future<var> value = new CompletableFuture<>();

    public DollarFuture(Future<var> value) {
        this.value = value;
    }

    @NotNull
    @Override
    public var $(String age, long l) {
        return getValue().$(age, l);
    }

    @NotNull
    @Override
    public var $(@NotNull String key) {
        return getValue().$(key);
    }

    @NotNull
    @Override
    public var $(@NotNull String key, Object value) {
        return getValue().$(key, value);
    }

    @NotNull
    @Override
    public <R> R $() {
        return getValue().$();
    }

    @NotNull
    @Override
    public var $error(@NotNull String errorMessage, @NotNull ErrorType type) {
        return getValue().$error(errorMessage, type);
    }

    @NotNull
    @Override
    public String string(@NotNull String key) {
        return getValue().string(key);
    }

    @Override
    public String $$() {
        return getValue().$$();
    }

    @Override
    public Integer integer() {
        return getValue().integer();
    }

    @Override
    public Integer integer(@NotNull String key) {
        return getValue().integer(key);
    }

    @NotNull
    @Override
    public JsonObject json() {
        return getValue().json();
    }

    @NotNull
    @Override
    public JsonObject json(@NotNull String key) {
        return getValue().json(key);
    }

    @NotNull
    @Override
    public List<var> $list() {
        return getValue().$list();
    }

    @Override
    public Map<String, Object> toMap() {
        return getValue().toMap();
    }

    @Override
    public Number number(@NotNull String key) {
        return getValue().number(key);
    }

    @NotNull
    @Override
    public JSONObject orgjson() {
        return getValue().orgjson();
    }

    @NotNull
    @Override
    public var $add(Object value) {
        return getValue().$add(value);
    }

    @NotNull
    @Override
    public Stream<var> $children() {
        return getValue().$children();
    }

    @NotNull
    @Override
    public Stream $children(@NotNull String key) {
        return getValue().$children(key);
    }

    @NotNull
    @Override
    public var $copy() {
        return getValue().$copy();
    }

    @Override
    public var decode() {
        return getValue().decode();
    }

    @Override
    public boolean equals(Object obj) {
        return getValue().equals(obj);
    }

    @Override
    public void err() {
        getValue().err();
    }

    @NotNull
    @Override
    public var $pipe(@NotNull String js) {
        return $pipe("anon", js);
    }

    @NotNull
    @Override
    public var $pipe(@NotNull String label, @NotNull String js) {
        return getValue().$pipe(label, js);
    }

    @Override
    public var eval(DollarEval lambda) {
        return eval("anon", lambda);
    }

    @Override
    public var eval(String label, DollarEval eval) {
        return getValue().eval(label, eval);
    }

    @Override
    public var eval(Class clazz) {
        return getValue().eval(clazz);
    }

    @NotNull
    @Override
    public var get(@NotNull Object key) {
        return getValue().get(key);
    }

    public Future<var> future() {
        return value;
    }


    @Override
    public boolean $has(@NotNull String key) {
        return getValue().$has(key);
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public var getOrDefault(Object key, var defaultValue) {
        return getValue().getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(@NotNull BiConsumer<? super String, ? super var> action) {
        getValue().forEach(action);
    }

    @Override
    public void replaceAll(@NotNull BiFunction<? super String, ? super var, ? extends var> function) {
        getValue().replaceAll(function);
    }

    @Override
    public var putIfAbsent(String key, var value) {
        return getValue().putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return getValue().remove(key, value);
    }

    @Override
    public boolean replace(String key, var oldValue, var newValue) {
        return getValue().replace(key, oldValue, newValue);
    }

    @Override
    public var replace(String key, var value) {
        return getValue().replace(key, value);
    }

    @Override
    public var computeIfAbsent(String key, @NotNull Function<? super String, ? extends var> mappingFunction) {
        return getValue().computeIfAbsent(key, mappingFunction);
    }

    @Override
    public var computeIfPresent(String key, @NotNull BiFunction<? super String, ? super var, ? extends var> remappingFunction) {
        return getValue().computeIfPresent(key, remappingFunction);
    }

    @Override
    public var compute(String key, @NotNull BiFunction<? super String, ? super var, ? extends var> remappingFunction) {
        return getValue().compute(key, remappingFunction);
    }

    @Override
    public var merge(String key, @NotNull var value, @NotNull BiFunction<? super var, ? super var, ? extends var> remappingFunction) {
        return getValue().merge(key, value, remappingFunction);
    }

    @Override
    public boolean $null() {
        return getValue().$null();
    }

    @Override
    public Stream<Map.Entry<String, var>> kvStream() {
        return getValue().kvStream();
    }

    @Override
    public Stream<String> keyStream() {
        return getValue().keyStream();
    }

    @NotNull
    @Override
    public var $load(@NotNull String location) {
        return getValue().$load(location);
    }

    @NotNull
    @Override
    public var $errors() {
        return getValue().$errors();
    }

    @NotNull
    @Override
    public String $mimeType() {
        return getValue().$mimeType();
    }

    @Override
    public void $out() {
        getValue().$out();
    }

    @NotNull
    @Override
    public var $pipe(@NotNull Class<? extends Script> clazz) {
        return getValue().$pipe(clazz);
    }

    @NotNull
    @Override
    public var $pipe(@NotNull Function<var, var> function) {
        return getValue().$pipe(function);
    }

    @NotNull
    @Override
    public var $pop(@NotNull String location, int timeoutInMillis) {
        return getValue().$pop(location, timeoutInMillis);
    }

    @NotNull
    @Override
    public var $pub(@NotNull String... locations) {
        return getValue().$pub(locations);
    }

    @NotNull
    @Override
    public var $push(@NotNull String location) {
       return  getValue().$push(location);
    }

    @Override
    public var remove(Object value) {
        return getValue().remove(value);
    }

    @NotNull
    @Override
    public var $rm(@NotNull String value) {
        return getValue().$rm(value);
    }

    @NotNull
    @Override
    public var $save(@NotNull String location) {
        return getValue().$save(location);
    }

    @NotNull
    @Override
    public var $save(@NotNull String location, int expiryInMilliseconds) {
        return getValue().$save(location, expiryInMilliseconds);
    }


    @NotNull
    @Override
    public var $set(@NotNull String key, Object value) {
        return getValue().$set(key, value);
    }

    @NotNull
    @Override
    public Stream<var> $stream() {
        return getValue().$stream();
    }

    @Override
    public List<String> strings() {
        return getValue().strings();
    }

    @NotNull
    @Override
    public String toString() {
        return getValue().toString();
    }

    @NotNull
    @Override
    public var _unwrap() {
        return getValue();
    }

    @NotNull
    @Override
    public Map<String, var> $map() {
        return getValue().$map();
    }

    var getValue() {
        try {
            var value = this.value.get();
            if(value == null) {
                return DollarStatic.handleError(new NullPointerException());
            }
            return value;
        } catch (InterruptedException ie) {
            return DollarStatic.handleInterrupt(ie);
        } catch (ExecutionException e) {
            return DollarStatic.handleError(e.getCause());
        } catch (Exception e) {
            return DollarStatic.handleError(e);
        }
    }

    public void setValue(var newValue) {
        if (value instanceof CompletableFuture) {
            ((CompletableFuture) value).complete(newValue);
        }
    }

    @Override
    public <R> R val() {
        return getValue().val();
    }

    @NotNull
    @Override
    public var $error(@NotNull String errorMessage) {
        return getValue().$error(errorMessage);
    }

    @NotNull
    @Override
    public var $error(@NotNull Throwable error) {
        return getValue().$error(error);
    }

    @NotNull
    @Override
    public var $error() {
        return getValue().$error();
    }

    @Override
    public boolean hasErrors() {
        return getValue().hasErrors();
    }

    @NotNull
    @Override
    public List<String> errorTexts() {
        return getValue().errorTexts();
    }

    @NotNull
    @Override
    public List<Throwable> errors() {
        return getValue().errors();
    }

    @Override
    public void clearErrors() {
        getValue().clearErrors();
    }

    @NotNull
    @Override
    public var $fail(@NotNull Consumer<List<Throwable>> handler) {
        return getValue().$fail(handler);
    }

    @NotNull
    @Override
    public var $null(@NotNull Callable<var> handler) {
        return getValue().$null(handler);
    }

    @NotNull
    @Override
    public var copy(@NotNull ImmutableList<Throwable> errors) {
        return getValue().copy(errors);
    }

    @Override
    public int size() {
        return getValue().size();
    }

    @Override
    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return getValue().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getValue().containsValue(value);
    }

    @Override
    public var put(String key, var value) {
        return getValue().put(key, value);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends var> m) {
        getValue().putAll(m);
    }

    @Override
    public void clear() {
        getValue().clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return getValue().keySet();
    }

    @NotNull
    @Override
    public Collection<var> values() {
        return getValue().values();
    }

    @NotNull
    @Override
    public Set<Entry<String, var>> entrySet() {
        return getValue().entrySet();
    }
}
