package org.cooder.units;

import static tech.units.indriya.function.AbstractConverter.IDENTITY;
import static tech.units.indriya.unit.Units.CUBIC_METRE;
import static tech.units.indriya.unit.Units.KELVIN;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.LITRE;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.RADIAN;
import static tech.units.indriya.unit.Units.SECOND;
import static tech.units.indriya.unit.Units.SQUARE_METRE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Area;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Time;
import javax.measure.quantity.Volume;

import org.cooder.units.quantity.Money;
import org.cooder.units.quantity.SKU;
import org.cooder.units.quantity.WorkTime;

import tech.units.indriya.AbstractUnit;
import tech.units.indriya.format.SimpleQuantityFormat;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.function.AddConverter;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.unit.AlternateUnit;
import tech.units.indriya.unit.TransformedUnit;

public final class Units {
    private static final tech.units.indriya.unit.Units BASIC_UNITS = tech.units.indriya.unit.Units.getInstance();

    private static final Set<Unit<?>> ALL_UNITS = new HashSet<>();
    private static final Map<String, Unit<?>> SYMBOL_MAP = new HashMap<>();
    private static final Map<String, Unit<?>> NAME_MAP = new HashMap<>();
    private static final Map<String, Unit<?>> parsedUnitCache = new ConcurrentHashMap<>();

    //
    // 无量纲单位
    //
    /**
     * @see tech.units.indriya.unit.AlternateUnit#ONE
     */
    public static final Unit<Dimensionless> 单位一 = AbstractUnit.ONE;

    /**
     * @see tech.units.indriya.unit.AlternateUnit#ONE
     */
    public static final Unit<Dimensionless> ONE = AbstractUnit.ONE;

    //
    // 长度单位
    //
    public static final Unit<Length> 米 = addUnit(METRE, "米");
    public static final Unit<Length> 延米 = addUnit(AlternateUnit.<Length>of(METRE, "延米", "延米"), "延米");
    public static final Unit<Length> 厘米 = addUnit(new TransformedUnit<>("cm", "厘米", METRE.divide(100), IDENTITY), "厘米");
    public static final Unit<Length> 毫米 = addUnit(new TransformedUnit<>("mm", "毫米", METRE.divide(1000), IDENTITY), "毫米");

    //
    // 面积单位
    //
    public static final Unit<Area> 平方米 = addUnit(new TransformedUnit<>("m²", "平米", SQUARE_METRE, IDENTITY), "平米");
    public static final Unit<Area> 平方厘米 = addUnit(new TransformedUnit<>("cm²", "平方厘米", SQUARE_METRE.divide(10000), IDENTITY), "平方厘米");
    public static final Unit<Area> 平方毫米 = addUnit(new TransformedUnit<>("mm²", "平方毫米", SQUARE_METRE.divide(1000000), IDENTITY), "平方毫米");

    //
    // 体积单位
    //
    public static final Unit<Volume> 立方米 = addUnit(new TransformedUnit<>("m³", "立方米", CUBIC_METRE, IDENTITY), "立方米");
    public static final Unit<Volume> 立方厘米 = addUnit(new TransformedUnit<>("cm³", "立方厘米", CUBIC_METRE.divide(1000000), IDENTITY), "立方厘米");
    public static final Unit<Volume> 立方毫米 = addUnit(new TransformedUnit<>("mm³", "立方毫米", CUBIC_METRE.divide(1000000000), IDENTITY), "立方毫米");

    public static final Unit<Volume> 升 = addUnit(LITRE, "升");
    public static final Unit<Volume> 毫升 = addUnit(new TransformedUnit<>("ml", "毫升", LITRE.divide(1000), IDENTITY), "毫升");

    //
    // 质量单位
    //
    public static final Unit<Mass> 千克 = addUnit(KILOGRAM, "千克");
    public static final Unit<Mass> 克 = addUnit(new TransformedUnit<>("g", "克", KILOGRAM.divide(1000), IDENTITY), "克");

    //
    // 温度单位 \u2103 = ℃
    //
    public static final Unit<Temperature> 摄氏度 = addUnit(new TransformedUnit<>("\u2103", "摄氏度", KELVIN, new AddConverter(273.15)), "摄氏度");

    //
    // 角度, 1° = π/180
    //
    public static final Unit<Angle> 角度 = addUnit(
            new TransformedUnit<>("°", "角度", RADIAN, MultiplyConverter.ofPiExponent(1).concatenate(MultiplyConverter.ofRational(1, 180))),
            "角度");

    //
    // 时间单位
    //
    public static final Unit<Time> 秒 = addUnit(SECOND, "秒钟");
    public static final Unit<Time> 分 = addUnit(new TransformedUnit<>("min", "分钟", SECOND.multiply(60), IDENTITY), "分钟");
    public static final Unit<Time> 小时 = addUnit(new TransformedUnit<>("hour", "小时", SECOND.multiply(3600), IDENTITY), "小时");
    public static final Unit<Time> 天 = addUnit(new TransformedUnit<>("day", "天", SECOND.multiply(86400), IDENTITY), "天");

    //
    // 人工耗时单位
    //
    public static final Unit<Dimensionless> 人 = addUnit(AlternateUnit.<Dimensionless>of(ONE, "man", "人"), "人");
    public static final Unit<WorkTime> 人时 = addUnit(new TransformedUnit<>("人时", "人时", 人.multiply(小时), IDENTITY).asType(WorkTime.class), "人时");
    public static final Unit<WorkTime> 人天 = addUnit(new TransformedUnit<>("人天", "人天", 人时.multiply(8), IDENTITY), "人天");

    //
    // 货币单位
    //
    public static final Unit<Money> 元 = addUnit(AlternateUnit.of(ONE, "元", "元").asType(Money.class), "元");
    public static final Unit<Money> 万元 = addUnit(new TransformedUnit<>("万元", "万元", 元.multiply(10000), IDENTITY).asType(Money.class), "万元");

    //
    // 未知单位
    //
    public static final Unit<Dimensionless> 未知 = addUnit(AlternateUnit.<Dimensionless>of(ONE, "未知", "未知"), "未知");

    //
    // SKU单位
    //
    private static final String[] SKU_UNITS_NAMES = new String[] {
            "根", "片", "条", "袋", "框", "套", "樘", "个", "台",
            "件", "只", "项", "扇", "卷", "桶", "盒", "张", "捆",
            "把", "架", "块", "瓶", "支", "箱", "付", "对", "次",
    };

    static {
        for (String s : SKU_UNITS_NAMES) {
            addSkuUnit(s, s);
        }
    }

    public static void init() {
        // no-op
    }

    /**
     * 通过单位的符号查找单位实例，如果symbol为空时返回无量纲单位, 即 {@link Units#ONE}
     * 
     * @param symbol 单位的符号，比如长度单位米: m
     * 
     * @return Unit实例
     */
    public static Unit<?> symbolFor(String symbol) {
        if(!notEmpty(symbol)) {
            return ONE;
        }

        Unit<?> u = SYMBOL_MAP.get(symbol);
        if(u == null) {
            u = getUnit(BASIC_UNITS.getUnits(), symbol);
        }
        return u;
    }

    /**
     * 通过单位的别名查找单位实例
     * 
     * @param name 单位别名，比如质量单位kg: 千克
     * 
     * @return Unit实例
     */
    public static Unit<?> nameFor(String name) {
        return NAME_MAP.get(name);
    }

    /**
     * 解析单位，当单位是组合单位时，使用这个方法。比如：<br>
     * "kg/桶"
     *
     * @param symbol 单位符号，比如速度单位: m/s
     *
     * @return Unit实例
     */
    public static Unit<?> parse(String symbol) {
        return parse(symbol, false);
    }

    public static Unit<?> parse(String symbol, boolean cache) {
        Unit<?> u = cache ? parsedUnitCache.get(symbol) : null;
        if (u == null) {
            String e = "0 " + symbol;
            u = SimpleQuantityFormat.getInstance("n u").parse(e).getUnit();
            if (cache) {
                parsedUnitCache.put(symbol, u);
            }
        }
        return u;
    }

    static Unit<?> getUnit(String string) {
        Unit<?> u = getUnit(ALL_UNITS, string);
        if(u == null) {
            u = getUnit(BASIC_UNITS.getUnits(), string);
        }
        return u;
    }

    /**
     * 为指定单位增加别名
     * 
     * @param unit  需要添加别名的单位
     * @param alias 别名
     * 
     * @throws IllegalArgumentException 如果单位不存在
     * @throws IllegalStateException    如果别名重复
     */
    public static <U extends Unit<?>> U addAlias(U unit, String alias) {
        if(!ALL_UNITS.contains(unit)) {
            throw new IllegalArgumentException("unit not exist.");
        }

        if(notEmpty(alias)) {
            Unit<?> pre = NAME_MAP.putIfAbsent(alias, unit);
            requireNull(pre);
            SimpleUnitFormat.getInstance().alias(unit, alias);
        }

        return unit;
    }

    /**
     * 增加自定义单位，不可以重复添加。<br>
     * 当添加了同符号的单位或同名的单位时，将会抛出异常。
     * 
     * @param unit  单位实例
     * @param alias 别名
     * @param <U>   单位的类型
     * 
     * @throws IllegalStateException 如果单位别名或单位符号重复
     * 
     * @return 参数中的单位实例
     */
    public static <U extends Unit<?>> U addUnit(U unit, String alias) {
        ALL_UNITS.add(unit);

        if(notEmpty(unit.getSymbol())) {
            Unit<?> pre = SYMBOL_MAP.putIfAbsent(unit.getSymbol(), unit);
            requireNull(pre);
            SimpleUnitFormat.getInstance().label(unit, unit.getSymbol());
        }

        if(notEmpty(alias)) {
            Unit<?> pre = NAME_MAP.putIfAbsent(alias, unit);
            requireNull(pre);
            SimpleUnitFormat.getInstance().alias(unit, alias);
        }

        return unit;
    }

    /**
     * 添加一个自定义的SKU单位
     * 
     * @param symbol 单位符号
     * @param name   单位名
     * 
     * @return 单位实例
     * 
     * @see Units#addUnit(Unit, String)
     */
    public static Unit<SKU> addSkuUnit(String symbol, String name) {
        return addUnit(AlternateUnit.<Dimensionless>of(ONE, symbol, name).asType(SKU.class), name);
    }

    static Collection<Unit<?>> all() {
        return new ArrayList<>(ALL_UNITS);
    }

    private static void requireNull(Unit<?> pre) {
        if(pre != null) {
            String msg = String.format("[%s] duplicated", pre);
            throw new IllegalStateException(msg);
        }
    }

    private static Unit<?> getUnit(Collection<Unit<?>> units, String string) {
        Objects.requireNonNull(string);
        return units.stream().filter(u -> string.equals(u.toString())).findAny().orElse(null);
    }

    private static boolean notEmpty(String str) {
        return str != null && str.length() > 0;
    }
}
