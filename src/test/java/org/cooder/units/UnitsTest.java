package org.cooder.units;

import java.util.Collection;

import javax.measure.Unit;
import javax.measure.quantity.Area;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnitsTest {

    static Collection<Unit<?>> all;

    @BeforeClass
    public static void setup() {
        Units.init();
        all = Units.all();
    }

    @Test
    public void testAll() {
        for (Unit<?> u : all) {
            System.out.println(String.format("symbol: %s, name:%s", u.getSymbol(), u.getName()));
            Assert.assertTrue(u.getSymbol() != null);
            Assert.assertTrue(u.getName() != null);
        }
    }

    @Test
    public void testAddUnit() {
        Unit<?> u = Units.addUnit(Units.元.multiply(10.12), "欧元");
        Assert.assertEquals(u, Units.parse("欧元"));

        String errMsg = null;
        try {
            Units.addUnit(Units.元.multiply(10.12), "欧元");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        Assert.assertEquals("[元*10.12] duplicated", errMsg);
    }

    @Test
    public void testSymbols() {
        Unit<?> u = Units.symbolFor("m");
        Assert.assertTrue(u.equals(Units.米));

        u = Units.symbolFor("K");
        Assert.assertTrue(u.equals(tech.units.indriya.unit.Units.KELVIN));

        u = Units.symbolFor("");
        Assert.assertEquals(u, Units.ONE);
    }

    @Test
    public void testAddAlias() {
        Units.addAlias(Units.平方米, "㎡");
        Unit<?> u = Units.nameFor("㎡");
        Assert.assertTrue(u.equals(Units.平方米));

        UnitNumber<Area> num = UnitNumber.parse("5 ㎡").asType(Area.class);
        Assert.assertTrue(num.getUnit().isEquivalentTo(Units.平方米));
        Assert.assertTrue("5 m²".equals(num.toString()));

        Units.addAlias(Units.平方米, "");

        String errMsg = null;
        try {
            Units.addAlias(Units.元.multiply(7.12), "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        Assert.assertEquals("unit not exist.", errMsg);
    }

    @Test
    public void testNames() {
        Unit<?> u = Units.nameFor("米");
        Assert.assertTrue(u.equals(Units.米));
    }

    @Test
    public void testGetUnit() {
        Unit<?> u = Units.getUnit("l");
        Assert.assertTrue(u.equals(Units.升));

        u = Units.getUnit("unknown");
        Assert.assertNull(u);
    }

    @Test
    public void testParse() {
        Unit<?> u = Units.parse("千克/桶");
        Assert.assertTrue("kg/桶".equals(u.toString()));

        Unit<?> u1 = Units.parse("千克/桶", true);
        Assert.assertTrue("kg/桶".equals(u1.toString()));
        Assert.assertTrue(u != u1);

        Unit<?> u2 = Units.parse("千克/桶", true);
        Assert.assertTrue(u1 == u2);
    }
}
