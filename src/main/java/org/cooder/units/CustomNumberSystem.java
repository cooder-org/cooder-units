package org.cooder.units;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import tech.units.indriya.function.DefaultNumberSystem;
import tech.units.indriya.function.RationalNumber;
import tech.units.indriya.spi.NumberSystem;

public class CustomNumberSystem implements NumberSystem {

    private DefaultNumberSystem defNumSystem = new DefaultNumberSystem();

    @Override
    public Number add(Number x, Number y) {
        return defNumSystem.add(x, y);
    }

    @Override
    public Number subtract(Number x, Number y) {
        return defNumSystem.subtract(x, y);
    }

    @Override
    public Number multiply(Number x, Number y) {
        return defNumSystem.multiply(x, y);
    }

    @Override
    public Number divide(Number x, Number y) {
        return defNumSystem.divide(x, y);
    }

    @Override
    public Number[] divideAndRemainder(Number x, Number y, boolean roundRemainderTowardsZero) {
        return defNumSystem.divideAndRemainder(x, y, roundRemainderTowardsZero);
    }

    @Override
    public Number power(Number number, int exponent) {
        return defNumSystem.power(number, exponent);
    }

    @Override
    public Number reciprocal(Number number) {
        return defNumSystem.reciprocal(number);
    }

    @Override
    public Number negate(Number number) {
        return defNumSystem.negate(number);
    }

    @Override
    public int signum(Number number) {
        return defNumSystem.signum(number);
    }

    @Override
    public Number abs(Number number) {
        return defNumSystem.abs(number);
    }

    @Override
    public Number exp(Number number) {
        return defNumSystem.exp(number);
    }

    @Override
    public Number log(Number number) {
        return defNumSystem.log(number);
    }

    @Override
    public Number narrow(Number number) {
        if(number instanceof BigDecimal) {
            final BigDecimal decimal = ((BigDecimal) number);
            if(decimal.stripTrailingZeros().scale() <= 0) {
                defNumSystem.narrow(number);
            } else {
                return number;
            }
        }
        return defNumSystem.narrow(number);
    }

    @Override
    public int compare(Number x, Number y) {
        return defNumSystem.compare(x, y);
    }

    @Override
    public boolean isZero(Number number) {
        return defNumSystem.isZero(number);
    }

    @Override
    public boolean isOne(Number number) {
        return defNumSystem.isOne(number);
    }

    @Override
    public boolean isLessThanOne(Number number) {
        return defNumSystem.isLessThanOne(number);
    }

    @Override
    public boolean isInteger(Number number) {
        return defNumSystem.isInteger(number);
    }

}
