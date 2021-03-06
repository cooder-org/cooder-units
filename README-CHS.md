
# cooder-units

![build](https://github.com/cooder-org/cooder-units/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/cooder-org/cooder-units/branch/main/graph/badge.svg?token=0L2AU184LV)](https://codecov.io/gh/cooder-org/cooder-units)
[![Maven Central](https://img.shields.io/maven-central/v/org.cooder/cooder-units.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.cooder%22%20AND%20a:%22cooder-units%22)

带单位计算库。

现实生活中的很多量都是带单位的，缺少单位时的数值本身没有任何含义，只进行数值运算后附加单位的做法非常容易导致bug。
不难理解，对于带单位量的计算过程，也应该让单位参与运算。
这个库通过封装jsr-385，提供了单位参与运算的能力，并内置了一些常见的非标准单位。

## 依赖
- 依赖 [jsr-385](https://jcp.org/aboutJava/communityprocess/mrel/jsr385/index.html)
- 依赖 jsr-385's reference implementation: [indriya](https://github.com/unitsofmeasurement/indriya)

## JDK版本
jdk1.8+

## 如何使用

0、引入依赖（Maven）  
```xml
<dependency>
  <groupId>org.cooder</groupId>
  <artifactId>cooder-units</artifactId>
  <version>${version}</version>
</dependency>
```

1、初始化
```java
    Units.init()
```

2、创建带单位的量
方法1: 
```java
  UnitNumber<?> 规格 = UnitNumber.parse("5 kg/桶");
```

方法2:
```java
  import static org.cooder.units.Units.*;
  
  UnitNumber<?> 规格 = new UnitNumber(5, 千克.divide(桶));
```
**建议优先使用方法2**

3、带单位加法
```java
  UnitNumber<Length> length = UnitNumber.parse("10 m").asType(Length.class);
  UnitNumber<Length> width = UnitNumber.parse("20 cm").asType(Length.class);
  UnitNumber<Length> sum = length.add(width);
```

4、带单位减法
```java
  UnitNumber<Length> length = UnitNumber.parse("10 m").asType(Length.class);
  UnitNumber<Length> width = UnitNumber.parse("20 cm").asType(Length.class);
  UnitNumber<Length> sub = length.subtract(width);
```

5、带单位乘法
```java
  UnitNumber<Length> length = UnitNumber.parse("10 m").asType(Length.class);
  UnitNumber<Length> width = UnitNumber.parse("20 cm").asType(Length.class);
  UnitNumber<?> area = length.multipy(width);
  
  // 如果需要校验结果单位是否符合预期，可以这样：
  area.assertMustBe(Units.平方米);
  
  // 或直接这样：
  UnitNumber<?> area = length.multipy(width).assertMustBe(Units.平方米);

```

6、带单位除法
```java
  UnitNumber<?> 规格 = UnitNumber.parse("5 kg/桶");
  UnitNumber<?> 使用量 = UnitNumber.parse("500 千克");
  UnitNumber<?> 下单量 = 使用量.divide(规格).assertMustBe(symbolFor("桶"));
  Assert.assertTrue(下单量.getValue().equals(100));
```

7、查找单位实例
```java
  // 通过单位符号
  Unit unit1 = Units.symbolFor("kg");
	
  // 通过单位别名
  Unit unit2 = Units.nameFor("千克");

  // 组合单位时
  Unit unit3 = Units.parse("千克/桶");
```

8、单位转换
```java
  UnitNumber<WorkTime> m = parse("10 人时").asType(WorkTime.class);
  m = m.to(Units.人天);
  Assert.assertTrue("1.25 人天".equals(m.toString()));
```

## 提示
乘法和除法时会将单位自动转化为国际单位制，如果需要转换为其他非标准单位，可以使用UnitNumber#to方法。

其他更多用法请参考项目的java doc.
