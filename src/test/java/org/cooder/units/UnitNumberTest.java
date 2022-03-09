package org.cooder.units;

import static org.cooder.units.UnitNumber.parse;
import static org.cooder.units.Units.symbolFor;
import static org.cooder.units.Units.千克;
import static org.cooder.units.Units.厘米;
import static org.cooder.units.Units.平方米;
import static org.cooder.units.Units.米;

import java.math.BigDecimal;

import javax.measure.quantity.Length;
import javax.measure.quantity.Time;

import org.cooder.units.quantity.Money;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnitNumberTest {
    @BeforeClass
    public static void setup() {
        Units.init();
    }

    @Test
    public void testConstruct() {
        UnitNumber<Length> len = new UnitNumber<>(10, 米);
        Assert.assertTrue(len.getValue().equals(10));
        Assert.assertTrue(len.getUnit().equals(米));
        Assert.assertTrue("10 m".equals(len.toString()));

        len = UnitNumber.parse("1 m 70 cm").asType(Length.class);
        Assert.assertTrue("1.7 m".equals(len.toString()));
    }

    @Test
    public void testAdd() {
        UnitNumber<Length> length = parse("10 m").asType(Length.class);
        UnitNumber<Length> perimeter = length.add(length);
        Assert.assertTrue("20 m".equals(perimeter.toString()));

        UnitNumber<Length> width = parse("20 cm").asType(Length.class);
        perimeter = length.add(width);
        Assert.assertTrue("10.2 m".equals(perimeter.toString()));
    }

    @Test
    public void testSub() {
        UnitNumber<Length> length = parse("10 m").asType(Length.class);
        UnitNumber<Length> perimeter = length.subtract(length);
        Assert.assertTrue("0 m".equals(perimeter.toString()));

        UnitNumber<Length> width = parse("20 cm").asType(Length.class);
        perimeter = length.subtract(width);
        Assert.assertTrue("9.8 m".equals(perimeter.toString()));
    }

    @Test
    public void testMultipy() {
        UnitNumber<Length> length = new UnitNumber<>(new BigDecimal("5.2"), 米);
        UnitNumber<Length> width = parse("3.2 m").asType(Length.class);
        UnitNumber<?> area = length.multiply(width).assertMustBe(平方米);
        BigDecimal espect = new BigDecimal("5.2").multiply(BigDecimal.valueOf(3.2));
        Assert.assertEquals(area.getValue(), espect);

        // 乘数值
        UnitNumber<Length> perimeter = length.multiply(4).assertMustBe(length.getUnit());
        espect = new BigDecimal("5.2").multiply(BigDecimal.valueOf(4));
        Assert.assertEquals(perimeter.getValue(), espect);

        // 不同尺度的单位
        width = parse("100 cm").asType(Length.class);
        area = length.multiply(width).assertMustBe(平方米);
        espect = new BigDecimal("5.2").multiply(BigDecimal.valueOf(1));
        Assert.assertEquals(area.getValue(), espect);
    }

    @Test
    public void testDivide() {
        UnitNumber<?> 规格 = UnitNumber.parse("5 kg/桶");
        UnitNumber<?> 使用量 = UnitNumber.parse("500 千克");
        UnitNumber<?> 下单量 = 使用量.divide(规格).assertIncludedInUnits().assertMustBe(symbolFor("桶"));
        Assert.assertTrue(下单量.getValue().equals(100));

        // 除以数值
        UnitNumber<?> t = 使用量.divide(2).assertMustBe(千克);
        Assert.assertTrue(t.getValue().equals(250));
    }

    @Test
    public void parseInvalid() {
        boolean hasException = false;
        try {
            UnitNumber.parse("5 m³2s/kg");
        } catch (IllegalArgumentException e) {
            hasException = true;
        }
        Assert.assertTrue(hasException);
    }

    @Test
    public void testDivideInvalid() {
        UnitNumber<?> 规格 = UnitNumber.parse("5 kg/桶");
        UnitNumber<?> 使用量 = UnitNumber.parse("500 l");
        UnitNumber<?> 下单量 = 使用量.divide(规格);
        Assert.assertTrue("m³·桶/kg".equals(下单量.getUnit().toString()));

        boolean hasException = false;
        try {
            下单量.assertIncludedInUnits();
        } catch (IllegalStateException e) {
            hasException = true;
        }
        Assert.assertTrue(hasException);

        hasException = false;
        try {
            下单量.assertMustBe(symbolFor("桶"));
        } catch (IllegalStateException e) {
            hasException = true;
        }
        Assert.assertTrue(hasException);
    }

    @Test
    public void testInverse() {
        UnitNumber<Time> t = parse("10 秒钟").asType(Time.class);
        UnitNumber<?> it = t.inverse();
        Assert.assertTrue("0.1 1/s".equals(it.toString()));
    }

    @Test
    public void testNegate() {
        UnitNumber<Money> m = parse("10 万元").asType(Money.class);
        m = m.negate();
        Assert.assertTrue("-10 万元".equals(m.toString()));
    }

    @Test
    public void testTo() {
        UnitNumber<Length> length = parse("10 m").asType(Length.class);
        length = length.to(厘米);
        Assert.assertTrue("1000 cm".equals(length.toString()));

        length = length.toSystemUnit();
        Assert.assertTrue("10 m".equals(length.toString()));
    }

    @Test
    public void testEquals() {
        UnitNumber<Length> l1 = parse("10 m").asType(Length.class);
        UnitNumber<Length> l2 = new UnitNumber<>(10, 米);
        UnitNumber<Length> l3 = new UnitNumber<>(1000, 厘米);

        Assert.assertEquals(l1, l2);
        Assert.assertEquals(l1.hashCode(), l2.hashCode());
        Assert.assertFalse(l1.equals(l3));
    }

    @Test
    public void testEq() {
        UnitNumber<Length> l1 = parse("10 m").asType(Length.class);
        UnitNumber<Length> l2 = new UnitNumber<>(1000, 厘米);
        Assert.assertTrue(l1.isEquivalentTo(l2));
    }

}
