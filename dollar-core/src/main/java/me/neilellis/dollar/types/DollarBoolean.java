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
import me.neilellis.dollar.var;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author <a href="http://uk.linkedin.com/in/neilellis">Neil Ellis</a>
 */
public class DollarBoolean extends AbstractDollarSingleValue<Boolean> {

    public DollarBoolean(@NotNull ImmutableList<Throwable> errors, @NotNull Boolean value) {
        super(errors, value);
    }

    @NotNull
    @Override
    public var $abs() {
        return this;
    }

    @NotNull @Override public var $minus(@NotNull var rhs) {
        var rhsFix = rhs._fixDeep();
        return DollarFactory.fromValue(value ^ rhsFix.isTruthy(), errors(), rhsFix.errors());
    }

    @NotNull
    @Override
    public var $negate() {
        return DollarFactory.fromValue(!value, errors());
    }

    @NotNull
    @Override
    public var $divide(@NotNull var rhs) {
        var rhsFix = rhs._fixDeep();
        return DollarFactory.fromValue(value == rhsFix.isTruthy(), errors(), rhsFix.errors());
    }

    @NotNull
    @Override
    public var $modulus(@NotNull var v) {
        return DollarFactory.failure(me.neilellis.dollar.types.ErrorType.INVALID_BOOLEAN_VALUE_OPERATION);
    }

    @NotNull
    @Override
    public var $multiply(@NotNull var v) {
        return DollarFactory.failure(me.neilellis.dollar.types.ErrorType.INVALID_BOOLEAN_VALUE_OPERATION);
    }

    @Override
    @NotNull
    public Integer I() {
        return value ? 1 : 0;
    }

    @NotNull
    @Override
    public Number N() {
        return value ? 1 : 0;
    }

    @Override public int sign() {
        return I();
    }

    @Override
    public var $as(Type type) {
        switch (type) {
            case BOOLEAN:
                return this;
            case STRING:
                return DollarStatic.$(S());
            case LIST:
                return DollarStatic.$(Arrays.asList(this));
            case MAP:
                return DollarStatic.$("value", this);
            case DECIMAL:
                return DollarStatic.$(value ? 1.0 : 0.0);
            case INTEGER:
                return DollarStatic.$(value ? 1 : 0);
            case VOID:
                return DollarStatic.$void();
            default:
                return DollarFactory.failure(me.neilellis.dollar.types.ErrorType.INVALID_CAST);

        }
    }

    @Override public Type $type() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean is(@NotNull Type... types) {
        for (Type type : types) {
            if (type == Type.BOOLEAN) {
                return true;
            }
        }
        return false;
    }

    @NotNull @Override public var $plus(var r) {
        var rhsFix = r._fixDeep();
        if (rhsFix.isList()) {
            rhsFix.$prepend(this);
        } else if (rhsFix.isList()) {
            return DollarFactory.fromValue(rhsFix.$prepend(this), errors(), rhsFix.errors());
        } else if (rhsFix.isRange()) {
            return DollarFactory.fromValue(rhsFix.$plus(this), errors(), rhsFix.errors());
        } else if (rhsFix.isString()) {
            return DollarFactory.fromValue(value.toString() + rhsFix.S(), errors(), rhsFix.errors());
        }
        return DollarFactory.fromValue(value || rhsFix.isTruthy(), errors(), rhsFix.errors());
    }

    @NotNull
    @Override
    public Double D() {
        return value ? 1.0 : 0.0;
    }

    @NotNull @Override
    public Long L() {
        return value ? 1L : 0L;
    }

    @Override
    public boolean isDecimal() {
        return false;
    }

    @Override
    public boolean isInteger() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DollarBoolean) {
            return toJavaObject().equals(((DollarBoolean) obj).toJavaObject());
        } else {
            return value.toString().equals(obj.toString());
        }
    }

    @Override
    public int compareTo(@NotNull var o) {
        return I() - o.I();
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isFalse() {
        return !value;
    }

    @Override
    public boolean isNeitherTrueNorFalse() {
        return false;
    }

    @Override
    public boolean isTrue() {
        return value;
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    @NotNull @Override public String toDollarScript() {
        return toString();
    }

    @NotNull
    @Override
    public Boolean toJavaObject() {
        return value;
    }
}
