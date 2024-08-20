package org.cooder.units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;

import org.cooder.units.quantity.UNKNOWN;

import tech.units.indriya.function.Calculus;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.ProductUnit;

/**
 * 表示现象、物体或物质的定量特性，比如质量、时间、距离、角度等。 <br>
 * 这个类是
 * <a href=
 * "https://jcp.org/aboutJava/communityprocess/mrel/jsr385/index.html">JSR-385</a>
 * 的核心接口
 * {@link javax.measure.Quantity}
 * 的封装。
 * 
 * @see javax.measure.Quantity
 * 
 * @author <a href="mailto:wuling@cooder.org">wuling</a>
 *
 * @param <Q> 量的类型
 */
public final class UnitNumber<Q extends Quantity<Q>> {
    public static final UnitNumber<?> UNKNOWN_NUMBER = new UnitNumber<>(1, Units.未知);

    private final Quantity<Q> q;

    public UnitNumber(Number value, Unit<Q> unit) {
        this.q = Quantities.getQuantity(value, unit);
    }

    private UnitNumber(Quantity<Q> q) {
        this(q.getValue(), q.getUnit());
    }

    public Number getValue() {
        return q.getValue();
    }

    public Unit<Q> getUnit() {
        return q.getUnit();
    }

    /**
     * 计算当前量与指定量的和，会自动进行单位转换
     * 
     * @param addend 加量
     * 
     * @return this + addend
     */
    public UnitNumber<Q> add(UnitNumber<Q> addend) {
        checkDimensionless(addend.getUnit());

        Quantity<Q> res = q.add(addend.q);
        return cast(res);
    }

    /**
     * 计算当前量与指定量的差，会自动进行单位转化
     * 
     * @param subtrahend 减数
     * 
     * @return this - subtrahend
     */
    public UnitNumber<Q> subtract(UnitNumber<Q> subtrahend) {
        checkDimensionless(subtrahend.getUnit());

        Quantity<Q> res = q.subtract(subtrahend.q);
        return cast(res);
    }

    /**
     * 计算当前量与指定数值的乘积，单位不会变化
     * 
     * @param multiplicand 乘数
     * 
     * @return this × multiplicand
     */
    public UnitNumber<Q> multiply(Number multiplicand) {
        Quantity<Q> res = q.multiply(multiplicand);
        return cast(res);
    }

    /**
     * 计算当前量与指定量的乘积，计算后结果会转换为国际单位制。
     * 如果需要转换为指定单位，可以使用{@link UnitNumber#to}
     * 
     * @param multiplicand 乘量
     * 
     * @return this × multiplicand
     */
    public UnitNumber<?> multiply(UnitNumber<?> multiplicand) {
        Quantity<?> res = q.toSystemUnit().multiply(multiplicand.q.toSystemUnit());
        return cast(res);
    }

    /**
     * 计算当前量与指定数值的商，单位不会变化
     * 
     * @param divisor 除数
     * 
     * @return this ÷ divisor
     */
    public UnitNumber<Q> divide(Number divisor) {
        Quantity<Q> res = q.divide(divisor);
        return cast(res);
    }

    /**
     * 计算当前量与指定量的商，计算后结果会转换为国际单位制。
     * 如果需要转换为指定单位，可以使用{@link UnitNumber#to}
     * 
     * @param divisor 除数
     * 
     * @return this ÷ divisor
     */
    public UnitNumber<?> divide(UnitNumber<?> divisor) {
        Quantity<?> res = q.toSystemUnit().divide(divisor.q.toSystemUnit());
        return cast(res);
    }

    /**
     * 求倒数，数值和单位都会求倒数，比如: <br>
     * "10 s" 的倒数是 "0.1 1/s"
     * 
     * @return 倒数
     */
    public UnitNumber<?> inverse() {
        Quantity<?> res = q.inverse();
        return cast(res);
    }

    /**
     * 求相反数
     *
     * @return {@code -this}.
     */
    public UnitNumber<Q> negate() {
        Quantity<Q> res = q.negate();
        return cast(res);
    }

    /**
     * 单位转换
     * 
     * @param unit 转换后的单位，必须保证转换后的单位是等价的
     * 
     * @return 单位转换后的量
     */
    public UnitNumber<Q> to(Unit<Q> unit) {
        checkDimensionless(unit);

        Quantity<Q> res = q.to(unit);
        return cast(res);
    }

    /**
     * 转换为国际标准单位制
     * 
     * @return 单位转换后量
     */
    public UnitNumber<Q> toSystemUnit() {
        return to(getUnit().getSystemUnit());
    }

    /**
     * 将当前量与指定量进行比较, 必要时进行单位转换
     *
     * @param that 指定量
     * 
     * @return {@code true} if {@code that ≡ this}.
     * 
     * @see javax.measure.Quantity#isEquivalentTo(Quantity)
     */
    public boolean isEquivalentTo(UnitNumber<Q> that) {
        checkDimensionless(that.getUnit());

        return q.isEquivalentTo(that.q);
    }

    /**
     * 将当前量与指定量进行比较, 如果两个量的单位不完全一致，会直接抛出异常
     * 
     * @param that 指定量
     * 
     * @return the value 0 if x == y; a value less than 0 if x &lt; y; and a
     *         value
     *         greater than 0 if x &gt; y
     * 
     * @throws IllegalStateException 如果两者的单位不完全一样
     */
    public int compareTo(UnitNumber<Q> that) {
        checkDimensionless(that.getUnit());
        return Calculus.currentNumberSystem().compare(this.getValue(), that.to(getUnit()).getValue());
    }

    public <T extends Quantity<T>> UnitNumber<T> asType(Class<T> type) throws ClassCastException {
        Quantity<T> res = q.asType(type);
        return cast(res);
    }

    /**
     * 判断当前量的单位与指定的单位是否一致，不一致时将抛出异常
     * 
     * @param that 指定的单位
     * 
     * @return this
     * 
     * @throws IllegalStateException 如果两者的单位不完全一样
     */
    public UnitNumber<Q> assertMustBe(Unit<?> that) {
        Unit<?> u = q.getUnit();
        if(!u.isCompatible(that) || !u.toString().equals(that.toString())) {
            throw unitNotMatch(u, that);
        }
        return this;
    }

    /**
     * 判断当前量的单位是否包含在标准单位中，不存在时抛出异常
     * 
     * @return this
     */
    public UnitNumber<Q> assertIncludedInUnits() {
        Unit<?> u = Units.getUnit(q.getUnit().toString());
        if(u == null) {
            String msg = String.format("[%s] is illegal", q.getUnit());
            throw new IllegalStateException(msg);
        }
        return this;
    }

    @Override
    public String toString() {
        return q.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UnitNumber) {
            return q.equals(((UnitNumber<?>) obj).q);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return q.hashCode();
    }

    private void checkDimensionless(Unit<Q> that) {
        Unit<Q> u = q.getUnit();
        if(q.getUnit().isCompatible(Units.ONE)) {
            if(q.getUnit().getSystemUnit() == that.getSystemUnit()) {
                return;
            }
            assertMustBe(that);
        }

        if(that instanceof ProductUnit && u instanceof ProductUnit) {
            ProductUnit<Q> pu1 = (ProductUnit<Q>) u;
            ProductUnit<Q> pu2 = (ProductUnit<Q>) that;
            if(pu1.getUnitCount() != pu2.getUnitCount()) {
                String msg = String.format("[%s] is not [%s]", u, that);
                throw new IllegalStateException(msg);
            }

            for (int i = 0; i < pu1.getUnitCount(); i++) {
                Unit<?> u1 = pu1.getUnit(i);
                Unit<?> u2 = pu2.getUnit(i);
                if(u1.isCompatible(Units.ONE)) {
                    assertMustEq(u1, u2);
                }
            }
        } else if(that instanceof ProductUnit || u instanceof ProductUnit) {
            assertMustEq(u, that);
        }
    }

    /**
     * 从字符串里解析出量，混合单位的量也可以解析。比如下面这些表达式都可以支持: <br>
     * <code>
     *     UnitNumber&lt;Length&gt; height = UnitNumber.parse("1.70 m").asType(Length.class);<br>
     *     UnitNumber&lt;Length&gt; height = UnitNumber.parse("170 cm").asType(Length.class);<br>
     *     UnitNumber&lt;Length&gt; height = UnitNumber.parse("1 m 70 cm").asType(Length.class);
     * </code>
     * 
     * @param cs 字符串
     * 
     * @return 解析出来的UnitNumber实例
     */
    public static UnitNumber<?> parse(CharSequence cs) {
        try {
            Quantity<?> q = Quantities.getQuantity(cs);
            return cast(q);
        } catch (MeasurementParseException e) {
            throw new IllegalArgumentException(e.getParsedString(), e);
        }
    }

    /**
     * number1 + number2
     * 
     * @param number1
     * @param number2
     * 
     * @return number1 + number2
     */
    public static UnitNumber<?> add(UnitNumber<?> number1, UnitNumber<?> number2) {
        return number1.asType(UNKNOWN.class).add(number2.asType(UNKNOWN.class));
    }

    /**
     * number1 - number2
     * 
     * @param number1
     * @param number2
     * 
     * @return number1 - number2
     */
    public static UnitNumber<?> subtract(UnitNumber<?> number1, UnitNumber<?> number2) {
        return number1.asType(UNKNOWN.class).subtract(number2.asType(UNKNOWN.class));
    }

    public static UnitNumber<?> to(UnitNumber<?> number1, Unit<?> unit) {
        return number1.asType(UNKNOWN.class).to(unit.asType(UNKNOWN.class));
    }

    /**
     * compare two number with unknown unit.
     * 
     * @param x one number
     * @param y another number
     * 
     * @return the value 0 if x == y; a value less than 0 if x &lt; y; and a
     *         value
     *         greater than 0 if x &gt; y
     * 
     * @throws IllegalStateException if unit not compatible
     */
    public static int compare(UnitNumber<?> x, UnitNumber<?> y) {
        UnitNumber<UNKNOWN> n1 = x.asType(UNKNOWN.class);
        UnitNumber<UNKNOWN> n2 = y.asType(UNKNOWN.class);
        return Calculus.currentNumberSystem().compare(n1.to(n2.getUnit()).getValue(), n2.getValue());
    }

    private static <T extends Quantity<T>> UnitNumber<T> cast(Quantity<T> q) {
        return new UnitNumber<>(q);
    }

    private static void assertMustEq(Unit<?> u, Unit<?> that) {
        if(!u.isCompatible(that) || !u.toString().equals(that.toString())) {
            throw unitNotMatch(u, that);
        }
    }

    private static IllegalStateException unitNotMatch(Unit<?> u, Unit<?> that) {
        String msg = String.format("[%s] is not [%s]", u, that);
        return new IllegalStateException(msg);
    }
}
