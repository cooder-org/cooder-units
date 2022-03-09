package org.cooder.units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;

import tech.units.indriya.quantity.Quantities;

/**
 * 表示现象、物体或物质的定量特性，比如质量、时间、距离、角度等。 <br/>
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
     * "1s" 的倒数是 "0.1 1/s"
     * 
     * @return
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
        Quantity<Q> res = q.to(unit);
        return cast(res);
    }

    /**
     * 转换为国际标准单位制
     * 
     * @return
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
        return q.isEquivalentTo(that.q);
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
     */
    public UnitNumber<Q> assertMustBe(Unit<?> that) {
        if (!q.getUnit().toString().equals(that.toString())) {
            String msg = String.format("[%s] is illegal", q.getUnit());
            throw new IllegalStateException(msg);
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
        if (u == null) {
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
        if (obj instanceof UnitNumber) {
            return q.equals(((UnitNumber<?>) obj).q);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return q.hashCode();
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
     * @return
     */
    public static UnitNumber<?> parse(CharSequence cs) {
        try {
            Quantity<?> q = Quantities.getQuantity(cs);
            return cast(q);
        } catch (MeasurementParseException e) {
            throw new IllegalArgumentException(e.getParsedString(), e);
        }
    }

    private static <T extends Quantity<T>> UnitNumber<T> cast(Quantity<T> q) {
        return new UnitNumber<>(q);
    }
}