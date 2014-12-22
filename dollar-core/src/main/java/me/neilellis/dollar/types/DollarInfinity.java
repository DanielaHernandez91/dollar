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

import me.neilellis.dollar.DollarStatic;
import me.neilellis.dollar.Type;
import me.neilellis.dollar.collections.ImmutableList;
import me.neilellis.dollar.collections.ImmutableMap;
import me.neilellis.dollar.collections.Range;
import me.neilellis.dollar.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static me.neilellis.dollar.DollarStatic.$void;

/**
 * To better understand the rationale behind this class, take a look at http://homepages.ecs.vuw.ac
 * .nz/~tk/publications/papers/void.pdf
 *
 * Dollar does not have the concept of null. Instead null {@link me.neilellis.dollar.var} objects are instances of this
 * class.
 *
 * Void is equivalent to 0,"",null except that unlike these values it has behavior that corresponds to a void object.
 *
 * Therefore actions taken against a void object are ignored. Any method that returns a {@link me.neilellis.dollar.var}
 * will return a {@link me.neilellis.dollar.types.DollarInfinity}.
 *
 * <pre>
 *
 *  var nulled= $null();
 *  nulled.$pipe((i)-&gt;{System.out.println("You'll never see this."});
 *
 * </pre>
 *
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public final class DollarInfinity extends AbstractDollar implements var {


    public static final var INSTANCE = DollarFactory.INFINITY;
    private boolean positive;

    public DollarInfinity(@NotNull ImmutableList<Throwable> errors, boolean positive) {
        super(errors);
        this.positive = positive;
    }

    public DollarInfinity(boolean positive) {

        super(ImmutableList.of());
        this.positive = positive;
    }

    @NotNull
    @Override
    public var $abs() {
        return DollarFactory.wrap(new DollarInfinity(errors(), true));
    }

    @NotNull
    @Override
    public var $minus(@NotNull var v) {
        return this;
    }

    @NotNull
    @Override
    public var $plus(var v) {
        return this;
    }

    @NotNull
    @Override
    public var $negate() {
        return DollarFactory.wrap(new DollarInfinity(errors(), !positive));
    }

    @NotNull
    @Override
    public var $divide(@NotNull var v) {
        return this;
    }

    @NotNull
    @Override
    public var $modulus(@NotNull var v) {
        return this;
    }

    @NotNull
    @Override
    public var $multiply(@NotNull var v) {
        if (v.isVoid()) {
            return $void();
        }
        if (v.$isEmpty().isTrue()) {
            return DollarFactory.fromValue(0, errors());
        }
        boolean
                positiveResult =
                positive && v.isPositive() || !(positive && isNegative()) && !(!positive && v.isPositive());
        return DollarFactory.wrap(new DollarInfinity(errors(), positiveResult));
    }

    @NotNull @Override
    public Integer I() {
        return positive ? Integer.MAX_VALUE : Integer.MIN_VALUE;
    }

    @NotNull
    @Override
    public Number N() {
        return positive ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    @Override public int sign() {
        return positive ? 1 : -1;
    }

    @NotNull
    @Override
    public var $get(@NotNull var key) {
        return this;
    }

    @NotNull @Override public var $append(@NotNull var value) {
        return this;
    }

    @NotNull @Override
    public var $containsValue(@NotNull var value) {
        return DollarStatic.$(false);
    }

    @NotNull @Override
    public var $has(@NotNull var key) {
        return DollarStatic.$(false);
    }

    @NotNull @Override
    public var $size() {
        return this;
    }

    @NotNull @Override public var $prepend(@NotNull var value) {
        return this;
    }

    @NotNull
    @Override
    public var $removeByKey(@NotNull String value) {
        return this;
    }

    @NotNull
    @Override
    public var $set(@NotNull var key, Object value) {
        return this;
    }

    @NotNull
    @Override
    public var remove(Object value) {
        return this;
    }

    @NotNull @Override public var $remove(var value) {
        return this;
    }

    @NotNull
    @Override
    public String S() {
        return positive ? "infinity" : "-infinity";
    }

    @Override
    public var $as(Type type) {
        if (type.equals(Type.BOOLEAN)) {
            return DollarStatic.$(true);
        } else if (type.equals(Type.STRING)) {
            return DollarStatic.$(positive ? "infinity" : "-infinity");
        } else if (type.equals(Type.LIST)) {
            return DollarStatic.$(Arrays.asList(this));
        } else if (type.equals(Type.MAP)) {
            return DollarStatic.$("value", this);
        } else if (type.equals(Type.DECIMAL)) {
            return DollarStatic.$(positive ? Double.MAX_VALUE : Double.MIN_VALUE);
        } else if (type.equals(Type.INTEGER)) {
            return DollarStatic.$(positive ? Long.MAX_VALUE : Long.MIN_VALUE);
        } else if (type.equals(Type.VOID)) {
            return $void();
        } else if (type.equals(Type.DATE)) {
            return this;
        } else if (type.equals(Type.RANGE)) {
            return DollarFactory.fromValue(new Range($(0), $(0)));
        } else {
            return DollarFactory.failure(me.neilellis.dollar.types.ErrorType.INVALID_CAST, type.toString(), false);
        }
    }

    @NotNull
    @Override
    public ImmutableList<var> $list() {
        return ImmutableList.of(this);
    }

    @Override public Type $type() {
        return Type.INFINITY;
    }

    @NotNull
    @Override
    public ImmutableMap<var, var> $map() {
        return ImmutableMap.of(DollarStatic.$("value"), this);
    }

    @Override
    public boolean is(@NotNull Type... types) {
        for (Type type : types) {
            if (type == Type.INFINITY || type == Type.INTEGER || type == Type.DECIMAL) {
                return true;
            }
        }
        return false;
    }

    @Override public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public ImmutableList<String> strings() {
        return ImmutableList.of();
    }

    @NotNull @Override public ImmutableList<Object> toList() {
        return ImmutableList.of(N());
    }

    @NotNull @Override
    public Map<String, Object> toMap() {
        return Collections.singletonMap("value", N());
    }

    @Override
    public int compareTo(@NotNull var o) {
        if (o.isInfinite()) {
            return new Integer(sign()).compareTo(o.sign());
        } else {
            if (positive) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof var) {
            var o = ((var) other)._fixDeep()._unwrap();
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            DollarInfinity that = (DollarInfinity) o;

            return positive == that.positive;
        } else {
            return false;
        }

    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isFalse() {
        return false;
    }

    @Override
    public boolean isNeitherTrueNorFalse() {
        return true;
    }

    @Override
    public boolean isTrue() {
        return false;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @NotNull @Override public String toDollarScript() {
        return positive ? "infinity" : "-infinity";
    }

    @Nullable
    @Override
    public <R> R toJavaObject() {
        return null;
    }

    @NotNull
    @Override
    public JSONObject toOrgJson() {
        return new JSONObject();
    }

}