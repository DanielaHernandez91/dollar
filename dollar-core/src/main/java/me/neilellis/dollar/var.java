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

package me.neilellis.dollar;

import me.neilellis.dollar.guard.*;
import me.neilellis.dollar.json.ImmutableJsonObject;
import me.neilellis.dollar.json.JsonArray;
import me.neilellis.dollar.json.JsonObject;
import me.neilellis.dollar.types.DollarFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public interface var extends ErrorAware, TypeAware, PipeAware, Serializable,
                             OldAndDeprecated, VarInternal, NumericAware, BooleanAware, ControlFlowAware,
                             AssertionAware, URIAware, MetadataAware, Comparable<var>, LogAware, StateAware<var>,
                             CollectionLike {


    /**
     * If this type supports the setting of Key/Value pairs this will set the supplied key value pair on a copy of this
     * object. If it doesn't an exception will be thrown. This method is a convenience method for the Java API.
     *
     * @param key   a String key for the value to be stored in this value.
     * @param value the value to add.
     *
     * @return the updated copy.
     */
    @NotNull
    @Guarded(ChainGuard.class)
    default var $(@NotNull String key, @Nullable Object value) {
        return $set(DollarStatic.$(key), DollarStatic.$(value));
    }

    /**
     * If this is a void object return v otherwise return this.
     *
     * @param v the object to return if this is void.
     *
     * @return this or v
     */
    @NotNull
    @Guarded(ChainGuard.class)
    @Guarded(ReturnVarOnlyGuard.class)
    @Guarded(NotNullParametersGuard.class) var $default(var v);

    /**
     * Returns the mime type of this {@link var} object. By default this will be 'application/json'
     *
     * @return the mime type associated with this object.
     */
    @NotNull
    @Guarded(ChainGuard.class)
    default var $mimeType() {
        return DollarStatic.$("application/json");
    }

    default String $serialized() {
        return DollarFactory.toJson(this).toString();
    }

    /**
     * Return the content of this object as a stream of values.
     *
     * @param parallel allow actions to be taken on the stream in parallel.
     *
     * @return a stream of values.
     */
    @NotNull Stream<var> $stream(boolean parallel);

    /**
     * Prints the S() value of this {@link var} to standard error.
     *
     * @return this
     */
    @NotNull
    @Guarded(ChainGuard.class)
    default var err() {
        System.err.println(toString());
        return this;
    }

    @NotNull
    @Guarded(NotNullGuard.class)
    @Override String toString();

    /**
     * Convert this object into a Dollar JsonArray.
     *
     * @return a JsonArray
     */
    @NotNull
    @Guarded(NotNullGuard.class)
    default JsonArray jsonArray() {
        return (JsonArray) DollarFactory.toJson(DollarStatic.$list(this));
    }

    /**
     * Prints the S() value of this {@link var} to standard out.
     */
    @NotNull
    @Guarded(ChainGuard.class)
    default var out() {
        System.out.println(toString());
        return this;
    }

    @NotNull String toDollarScript();

    /**
     * Returns a {@link me.neilellis.dollar.json.JsonObject}, JsonArray or primitive type such that it can be added to
     * either a {@link me.neilellis.dollar.json.JsonObject} or JsonArray.
     *
     * @return a Json friendly object.
     */
    @Nullable <R> R toJavaObject();

    /**
     * Returns this object as a org.json.JSONObject.
     *
     * NB: This conversion is quite efficient.
     *
     * @return a JSONObject
     */
    @Nullable
    default JSONObject toOrgJson() {
        ImmutableJsonObject json = toJsonObject();
        if (json != null) {
            return new JSONObject(json.toMap());
        } else {
            return null;
        }
    }

    /**
     * Convert this to a Dollar JsonObject
     *
     * @return this as a JsonObject
     */
    default @Nullable ImmutableJsonObject toJsonObject() {
        return new ImmutableJsonObject((JsonObject) toJsonType());
    }

    default Object toJsonType() {
        return DollarFactory.toJson(this);
    }

}
