package com.maoyu.bean.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author maoyu [2018-10-08 11:03]
 **/
public class SafeMath {

    /**
     * 零
     */
    protected static final Pattern ZERO = Pattern.compile("^(0)+(\\.0+)?$");
    /**
     * 十六进制
     */
    protected static final Pattern HEX = Pattern.compile("^0[xX][0-9A-Fa-f]+$");

    /**
     * 十进制数字
     */
    private static final String Digits = "(\\p{Digit}+)";
    /**
     * 十六进制数字
     */
    private static final String HexDigits = "(\\p{XDigit}+)";
    private static final String Exp = "[eE][+-]?" + Digits;

    protected static final Pattern FP_NUMBER = Pattern.compile(
            "[+-]?((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
                    "(\\.(" + Digits + ")(" + Exp + ")?)|" +
                    "(0[xX]" + HexDigits + "(\\.)?)|" +
                    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + "))");

    /**
     * 获取常量零
     *
     * @param toClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T getZero(Class<T> toClass) {
        if (BigDecimal.class.isAssignableFrom(toClass)) {
            return (T) BigDecimal.ZERO;
        } else if (BigInteger.class.isAssignableFrom(toClass)) {
            return (T) BigInteger.ZERO;
        } else if (Byte.class.equals(toClass)) {
            return (T) Byte.valueOf((byte) 0);
        } else if (Double.class.equals(toClass)) {
            return (T) Double.valueOf(0.0d);
        } else if (Float.class.equals(toClass)) {
            return (T) Float.valueOf(0.0f);
        } else if (Integer.class.equals(toClass)) {
            return (T) Integer.valueOf(0);
        } else if (Long.class.equals(toClass)) {
            return (T) Long.valueOf(0L);
        } else if (Short.class.equals(toClass)) {
            return (T) Short.valueOf((short) 0);
        } else {
            // 根据类型，字段名称和字段类型查询一个字段
            Field f = ReflectionUtils.findField(toClass, "ZERO", toClass);
            // 判断字段是否是public static final的；
            if (f != null && ReflectionUtils.isPublicStaticFinal(f)) {
                try {
                    return (T) f.get(null);
                } catch (IllegalAccessException e) {
                }
            }
            //将Number转为指定的类型
            return NumberUtils.convertNumberToTargetClass(0, toClass);
        }
    }

    /**
     * 判断是否为零
     *
     * <ul>
     * <li>Null</li>
     * <li>空和空串</li>
     * <li>字符串只有0 (如：000) 或带有十进制的0(如：000.0000)</li>
     * <li>等于0的数字</li>
     * <li>布尔类型：false</li>
     * <li>不能转换成数字的字符串和其他对象</li>
     * </ul>
     *
     * @param value
     * @return
     */
    public static boolean isZero(Object value) {
        if (value == null || Boolean.FALSE.equals(value)) {
            return true;
        }
        if (value instanceof CharSequence) {
            if (StringUtils.isBlank((CharSequence) value)) {
                return true;
            } else if (ZERO.matcher((CharSequence) value).matches()) {
                return true;
            } else if (!isNumber(value.toString())) {
                return true;
            }
        }
        if (value instanceof Number) {
            if (value instanceof BigDecimal) {
                return BigDecimal.ZERO.compareTo((BigDecimal) value) == 0;
            } else if (value instanceof BigInteger) {
                return BigInteger.ZERO.compareTo((BigInteger) value) == 0;
            } else if (value instanceof Byte) {
                return ((Number) value).byteValue() == (byte) 0;
            } else if (value instanceof Short) {
                return ((Number) value).shortValue() == (short) 0;
            } else if (value instanceof Integer) {
                return ((Number) value).intValue() == 0;
            } else if (value instanceof Long) {
                return ((Number) value).longValue() == 0L;
            } else if (value instanceof Float) {
                return ((Number) value).floatValue() == 0f;
            } else {
                return ((Number) value).doubleValue() == 0.0d;
            }
        } else {
            String svalue = value.toString();
            if (HEX.matcher(svalue).matches()) {
                return BigInteger.ZERO
                        .compareTo(value(BigInteger.class, svalue)) == 0;
            } else {
                return BigDecimal.ZERO
                        .compareTo(value(BigDecimal.class, svalue)) == 0;
            }
        }
    }

    /**
     * 将对象转换为数字
     * <p>
     * 如果值是数字，它将被转换成合适的类型
     * 否则它将被转换为字符串并解析成数字
     * 如果不能解析，将返回零。
     *
     * @param toClass
     * @param value
     * @param <T>
     * @return
     * @throws IllegalArgumentException
     */
    public static <T extends Number> T value(Class<T> toClass, Object value) {
        checkNull(toClass);
        // 如果参数为空、为false、正则符合 返回0
        if (value == null || Boolean.FALSE.equals(value) || ZERO.matcher(value.toString()).matches()) {
            return getZero(toClass);
        } else if (toClass.isInstance(value)) {
            // 如果value为toClass的类型或接口实例，则转换为该类型
            return toClass.cast(value);
        } else if (value instanceof Number) {
            // 如果value为Number的类型或接口实例，则将value转化为toClass类型
            return NumberUtils.convertNumberToTargetClass((Number) value,
                    toClass);
        } else {
            try {
                String svalue = value.toString();
                char dot = ((DecimalFormat) DecimalFormat.getInstance())
                        .getDecimalFormatSymbols().getDecimalSeparator();
                if (svalue.indexOf(dot) > -1) {
                    // 将小数转化为BigDecimal
                    return NumberUtils.convertNumberToTargetClass(
                            // 将字符串转化为BigDecimal类型
                            NumberUtils.parseNumber(svalue, BigDecimal.class),
                            toClass);
                } else if (HEX.matcher(svalue).matches()) {
                    // 将十六进制转换为BigInteger
                    return NumberUtils.convertNumberToTargetClass(
                            NumberUtils.parseNumber(svalue, BigInteger.class),
                            toClass);
                } else {
                    return NumberUtils.parseNumber(svalue, toClass);
                }
            } catch (Exception e) {
                return getZero(toClass);
            }
        }
    }

    /**
     * 集合的加法
     * <p>
     * 如果集合为空则返回零
     * 集合里面的对象为空则忽略该对象
     *
     * @param values
     * @param toClass
     * @param <T>
     * @return
     */
    public static <T extends Number> T sum(Collection<? extends Number> values, Class<T> toClass) {
        checkNull(toClass);
        if (values == null || values.isEmpty()) {
            return getZero(toClass);
        } else if (BigInteger.class.isAssignableFrom(toClass)
                || BigDecimal.class.isAssignableFrom(toClass)) {
            BigDecimal sum = BigDecimal.ZERO;
            for (Number v : values) {
                if (v != null) {
                    if (v instanceof BigDecimal) {
                        sum = sum.add((BigDecimal) v);
                    } else if (v instanceof Short || v instanceof Long
                            || v instanceof Integer) {
                        sum = sum.add(BigDecimal.valueOf(v.longValue()));
                    } else {
                        sum = sum.add(NumberUtils.convertNumberToTargetClass(v,
                                BigDecimal.class));
                    }
                }
            }
            return NumberUtils.convertNumberToTargetClass(sum, toClass);
        } else {
            double sum = 0.0;
            for (Number v : values) {
                if (v != null) {
                    sum += v.doubleValue();
                }
            }
            return NumberUtils.convertNumberToTargetClass(sum, toClass);
        }
    }

    /**
     * 集合的乘法
     * <p>
     * 集合为空则返回零
     *
     * @param values
     * @param toClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T product(Collection<? extends Number> values, Class<T> toClass) {
        checkNull(toClass);
        if (values == null || values.isEmpty()) {
            return getZero(toClass);
        }
        boolean typeMatch = true;
        for (Number v : values) {
            if (v != null && !toClass.isInstance(v)) {
                typeMatch = false;
                break;
            }
        }
        if (typeMatch) {
            if (BigDecimal.class.isAssignableFrom(toClass)) {
                BigDecimal product = BigDecimal.ONE;
                for (Number v : values) {
                    BigDecimal multiplier = (BigDecimal) v;
                    if (multiplier == null
                            || BigDecimal.ZERO.compareTo(multiplier) == 0) {
                        return (T) BigDecimal.ZERO;
                    }
                    product = product.multiply(multiplier);
                }
                return (T) product;
            } else if (BigInteger.class.isAssignableFrom(toClass)) {
                BigInteger product = BigInteger.ONE;
                for (Number v : values) {
                    BigInteger multiplier = (BigInteger) v;
                    if (multiplier == null
                            || BigInteger.ZERO.compareTo(multiplier) == 0) {
                        return (T) BigInteger.ZERO;
                    }
                    product = product.multiply(multiplier);
                }
                return (T) product;
            } else if (Byte.class.equals(toClass)
                    || Integer.class.equals(toClass) || Long.class.equals(toClass)
                    || Short.class.equals(toClass)) {
                long product = 1L;
                for (Number v : values) {
                    if (v == null) {
                        return getZero(toClass);
                    }
                    long multiplier = v.longValue();
                    if (multiplier == 0) {
                        return getZero(toClass);
                    }
                    product *= multiplier;
                }
                return NumberUtils.convertNumberToTargetClass(product, toClass);
            } else if (Float.class.equals(toClass)
                    || Double.class.equals(toClass)) {
                double product = 1.0;
                for (Number v : values) {
                    if (v == null) {
                        return getZero(toClass);
                    }
                    double multiplier = v.doubleValue();
                    if (multiplier == 0.0) {
                        return getZero(toClass);
                    }
                    product *= multiplier;
                }
                return NumberUtils.convertNumberToTargetClass(product, toClass);
            }
        }
        // 其他情况
        BigDecimal product = BigDecimal.ONE;
        for (Number v : values) {
            if (v == null) {
                return getZero(toClass);
            } else {
                BigDecimal multiplier = v instanceof BigDecimal ? (BigDecimal) v
                        : value(BigDecimal.class, v);
                if (BigDecimal.ZERO.compareTo(multiplier) == 0) {
                    return getZero(toClass);
                }
                product = product.multiply(multiplier);
            }
        }
        return value(toClass, product);
    }

    /**
     * 两个数的加法
     * <p>
     * 如果加数为null、空、或非数字，则变成零
     *
     * @param a       加数
     * @param b       加数
     * @param toClass
     * @param <T>
     * @return
     */
    public static <T extends Number> T add(Object a, Object b, Class<T> toClass) {
        return doAdd(a, b, toClass, false);
    }

    /**
     * 两个数的减法
     * <p>
     * 如果减少或被减数为null、空、或非数字，则变成零
     *
     * @param a       减数
     * @param b       被减数
     * @param toClass
     * @param <T>
     * @return
     */
    public static <T extends Number> T subtract(Object a, Object b, Class<T> toClass) {
        return doAdd(a, b, toClass, true);
    }

    /**
     * 两个数相加或相减的实现方法
     *
     * @param a
     * @param b
     * @param toClass
     * @param subtract false-加法 true-减法
     * @param <T>
     * @return
     */
    private static <T extends Number> T doAdd(Object a, Object b, Class<T> toClass, boolean subtract) {
        checkNull(toClass);
        if (a == null && b == null) {
            return getZero(toClass);
        }
        if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
                || Long.class.equals(toClass) || Short.class.equals(toClass)) {
            long a1 = value(Long.class, a);
            long b1 = value(Long.class, b);
            return NumberUtils.convertNumberToTargetClass(subtract ? a1 - b1
                    : a1 + b1, toClass);
        } else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
            double a1 = value(Double.class, a);
            double b1 = value(Double.class, b);
            return NumberUtils.convertNumberToTargetClass(subtract ? a1 - b1
                    : a1 + b1, toClass);
        } else {
            BigDecimal a1 = value(BigDecimal.class, a);
            BigDecimal b1 = value(BigDecimal.class, b);
            return NumberUtils.convertNumberToTargetClass(
                    subtract ? a1.subtract(b1) : a1.add(b1), toClass);
        }
    }

    /**
     * 两个数的乘法
     * <p>
     * 其中一个乘数或被乘数为null、0、不为数字， 则返回结果为零
     *
     * @param a       乘数
     * @param b       被乘数
     * @param toClass 被转化为该类型
     * @param <T>     返回数字类型
     * @return
     */
    public static <T extends Number> T multiply(Object a, Object b, Class<T> toClass) {
        checkNull(toClass);
        if (a == null || b == null) {
            return getZero(toClass);
        } else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
                || Long.class.equals(toClass) || Short.class.equals(toClass)) {
            long multiplicand = value(Long.class, a);
            long multiplier = value(Long.class, b);
            if (multiplicand == 0L || multiplier == 0L) {
                return getZero(toClass);
            } else {
                return NumberUtils.convertNumberToTargetClass(multiplicand
                        * multiplier, toClass);
            }
        } else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
            double multiplicand = value(Double.class, a);
            double multiplier = value(Double.class, b);
            if (multiplicand == 0.0 || multiplier == 0.0) {
                return getZero(toClass);
            } else {
                return NumberUtils.convertNumberToTargetClass(multiplicand
                        * multiplier, toClass);
            }
        } else {
            BigDecimal multiplicand = value(BigDecimal.class, a);
            BigDecimal multiplier = value(BigDecimal.class, b);
            if (BigDecimal.ZERO.compareTo(multiplicand) == 0
                    || BigDecimal.ZERO.compareTo(multiplier) == 0) {
                return getZero(toClass);
            } else {
                return NumberUtils.convertNumberToTargetClass(
                        multiplicand.multiply(multiplier), toClass);
            }
        }
    }

    /**
     * 两个数的除法
     * <p>
     * 其中一个参数为null、0、不为数字， 则返回结果为零
     *
     * @param a       除数
     * @param b       被除数
     * @param toClass 被转化为该类型
     * @param <T>     返回数字类型
     * @return
     */
    public static <T extends Number> T divide(Object a, Object b, Class<T> toClass) {
        checkNull(toClass);
        if (isZero(a) || isZero(b)) {
            return getZero(toClass);
        } else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
                || Long.class.equals(toClass) || Short.class.equals(toClass)) {
            long dividend = value(Long.class, a);
            long divisor = value(Long.class, b);
            if (dividend == 0L || divisor == 0L) {
                return getZero(toClass);
            } else {
                return NumberUtils.convertNumberToTargetClass(dividend
                        / divisor, toClass);
            }
        } else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
            double dividend = value(Double.class, a);
            double divisor = value(Double.class, b);
            if (dividend == 0.0 || divisor == 0.0) {
                return getZero(toClass);
            } else {
                return NumberUtils.convertNumberToTargetClass(dividend
                        / divisor, toClass);
            }
        } else {
            BigDecimal multiplicand = value(BigDecimal.class, a);
            BigDecimal multiplier = value(BigDecimal.class, b);
            if (BigDecimal.ZERO.compareTo(multiplicand) == 0
                    || BigDecimal.ZERO.compareTo(multiplier) == 0) {
                return getZero(toClass);
            } else {
                // 结果保留10位小数，且是四舍五入的舍入模式
                return NumberUtils.convertNumberToTargetClass(multiplicand
                        .divide(multiplier, 10, RoundingMode.HALF_UP)
                        .stripTrailingZeros(), toClass);
            }
        }
    }


    /**
     * 转换为scale位小数点，舍入模式为四舍五入
     *
     * @param a
     * @param scale   小数点位数
     * @param toClass
     * @param <T>
     * @return
     */
    public static <T extends Number> T scale(Number a, int scale, Class<T> toClass) {
        checkNull(toClass);
        if (isZero(a) || isZero(scale)) {
            return getZero(toClass);
        }
        BigDecimal b = new BigDecimal(a.toString());
        return NumberUtils.convertNumberToTargetClass(b
                .divide(new BigDecimal("1"), scale, RoundingMode.HALF_UP)
                .stripTrailingZeros(), toClass);
    }

    /**
     * 是否为整数
     * <p>
     * 属于整数的有：1.0f, 2.0d, new BigDecimal("123.0000"), 456L, "123.00"
     * 不属于整数的有：1.2f, 2.123d, new BigDecimal("123.456"), "1231123123123123123123123123"（超出范围）, "hello"
     *
     * @param value
     * @return
     */
    public static boolean isInteger(Object value) {
        if (value instanceof CharSequence) {
            String svalue = value.toString();
            if (isNumber(svalue)) {
                char dot = ((DecimalFormat) DecimalFormat.getInstance())
                        .getDecimalFormatSymbols().getDecimalSeparator();
                value = svalue.indexOf(dot) > -1 ? value(BigDecimal.class,
                        svalue) : value(BigInteger.class, svalue);
            }
        }
        if (value instanceof Byte || value instanceof Short
                || value instanceof Integer) {
            return true;
        } else if (value instanceof Long || value instanceof BigInteger) {
            return ((Number) value).longValue() < Integer.MAX_VALUE
                    && ((Number) value).longValue() > Integer.MIN_VALUE;
        } else if (value instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) value;
            return (bd.signum() == 0 || bd.scale() <= 0 || bd
                    .stripTrailingZeros().scale() <= 0)
                    && bd.longValue() < Integer.MAX_VALUE
                    && bd.longValue() > Integer.MIN_VALUE;
        } else if (value instanceof Double || value instanceof Float
                || value instanceof Number) {
            double dvalue = ((Number) value).doubleValue();
            return dvalue == Math.floor(dvalue) && !Double.isInfinite(dvalue)
                    && dvalue < Integer.MAX_VALUE && dvalue > Integer.MIN_VALUE;
        }
        return false;
    }

    /**
     * 两个数的百分比
     *
     * @param numerator   除数
     * @param denominator 被除数
     * @param toClass
     * @param <T>
     * @return
     */
    public static <T extends Number> T percentify(Object numerator, Object denominator, Class<T> toClass) {
        return multiply(divide(numerator, denominator, toClass), 100, toClass);
    }


    /**
     * 字符串是否为有效数字
     *
     * @param cs
     * @return
     */
    public static boolean isNumber(CharSequence cs) {
        return cs != null && FP_NUMBER.matcher(cs).matches();
    }


    /**
     * 判空
     *
     * @param object
     * @param <T>
     * @return
     */
    private static <T> T checkNull(T object) {
        if (object == null) {
            throw new IllegalArgumentException("This argument is required; it cannot be null");
        }
        return object;
    }
}
