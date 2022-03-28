package org.cooder.units;

import javax.measure.Unit;
import javax.measure.quantity.Area;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnitsTest {
    @BeforeClass
    public static void setup() {
        Units.init();
    }

    @Test
    public void testSymbols() {
        Unit<?> u = Units.symbolFor("m");
        Assert.assertTrue(u.equals(Units.米));

        u = Units.symbolFor("K");
        Assert.assertTrue(u.equals(tech.units.indriya.unit.Units.KELVIN));
    }

    @Test
    public void testAddAlias() {
        Units.addAlias(Units.平方米, "㎡");
        Unit<?> u = Units.nameFor("㎡");
        Assert.assertTrue(u.equals(Units.平方米));

        UnitNumber<Area> num = UnitNumber.parse("5 ㎡").asType(Area.class);
        Assert.assertTrue(num.getUnit().isEquivalentTo(Units.平方米));
        Assert.assertTrue("5 m²".equals(num.toString()));
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
    }
}
