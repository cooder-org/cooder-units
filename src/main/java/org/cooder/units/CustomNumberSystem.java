package org.cooder.units;

import java.math.BigDecimal;

import tech.units.indriya.function.DefaultNumberSystem;

public class CustomNumberSystem extends DefaultNumberSystem {

    @Override
    public Number narrow(Number number) {
        if(number instanceof BigDecimal) {
            final BigDecimal decimal = ((BigDecimal) number);
            if(decimal.stripTrailingZeros().scale() <= 0) {
                super.narrow(number);
            } else {
                return number;
            }
        }
        return super.narrow(number);
    }
}
