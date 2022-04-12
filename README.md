
# cooder-units

![build](https://github.com/cooder-org/cooder-units/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/cooder-org/cooder-units/branch/main/graph/badge.svg?token=0L2AU184LV)](https://codecov.io/gh/cooder-org/cooder-units)
[![Maven Central](https://img.shields.io/maven-central/v/org.cooder/cooder-units.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.cooder%22%20AND%20a:%22cooder-units%22)

[中文版](README-CHS.md)

Many quantities in real world have units, and the value itself has no meaning when the unit is missing. The practice of only adding units after numerical calculation is very easy to cause bugs.
It is not difficult to understand that for the calculation process with unit quantities, the unit should also be involved in the operation.

Currently, developers have to either use an inadequate model of measurement or to create a custom solution. Either choice can lead to significant programmatic errors. The default practice of modeling a measure as a simple 
number with no regard to the units it represents creates fragile code, as the value may easily be misinterpreted if the unit must be inferred solely from contextual clues.

This library supporting robust representation and correct handling of quantities, by encapsulating jsr-385, and has built-in some common non-standard units.

## Dependencies
- base on [jsr-385](https://jcp.org/aboutJava/communityprocess/mrel/jsr385/index.html)
- base on jsr-385's reference implementation: [indriya](https://github.com/unitsofmeasurement/indriya)

## JDK version
jdk1.8+

## quick start

0、config (maven)  
```xml
<dependency>
  <groupId>org.cooder</groupId>
  <artifactId>cooder-units</artifactId>
  <version>1.0.2</version>
</dependency>
```

1、initialization units
```java
    Units.init()
```

2、create quantity with units
method 1: 
```java
  UnitNumber<?> speed = UnitNumber.parse("5 m/s");
```

method 2: (recommend)
```java
  import static tech.units.indriya.unit.Units.*;
  
  UnitNumber<?> speed = new UnitNumber(5, METRE.divide(SECOND));
```

3、add
```java
  UnitNumber<Length> length = UnitNumber.parse("10 m").asType(Length.class);
  UnitNumber<Length> width = UnitNumber.parse("20 cm").asType(Length.class);
  UnitNumber<Length> sum = length.add(width);
```

4、subtract
```java
  UnitNumber<Length> length = UnitNumber.parse("10 m").asType(Length.class);
  UnitNumber<Length> width = UnitNumber.parse("20 cm").asType(Length.class);
  UnitNumber<Length> sub = length.subtract(width);
```

5、multipy
```java
  import static tech.units.indriya.unit.Units.*;

  UnitNumber<Length> length = UnitNumber.parse("10 m").asType(Length.class);
  UnitNumber<Length> width = UnitNumber.parse("20 cm").asType(Length.class);
  UnitNumber<?> area = length.multipy(width);
  
  // If you need to verify that the result unit is as expected, you can do like this：
  area.assertMustBe(SQUARE_METRE);
  
  // or directly like this：
  UnitNumber<?> area = length.multipy(width).assertMustBe(SQUARE_METRE);

```

6、divide
```java
  import static tech.units.indriya.unit.Units.*;
  
  UnitNumber<?> spec = UnitNumber.parse("1 kg/l");
  UnitNumber<?> useQuantity = UnitNumber.parse("100 kg");
  UnitNumber<?> orderQuantity = useQuantity.divide(spec).assertMustBe(LITRE);
  
  Assert.assertTrue(orderQuantity.getValue().equals(100));
```

7、find the instance of Unit
```java
  // find by unit symbol
  Unit unit1 = Units.symbolFor("kg");

  // combination unit 
  Unit unit3 = Units.parse("m/s");
```

8、unit conversion 
```java
  import static tech.units.indriya.unit.Units.*;
  
  UnitNumber<Mass> m = parse("1250 g").asType(Mass.class);
  m = m.to(KILOGRAM);
  Assert.assertTrue("1.25 kg".equals(m.toString()));
```

## hint
Units are automatically converted to SI units during multiplication and division. If you need to convert to other non-standard units, you can use the UnitNumber#to method.

For more usage, please refer to the java doc of the project.
