package com.maoyu.bean.util;

import org.testng.annotations.Test;

import java.math.BigDecimal;

/**
 * @author maoyu [2018-10-17 14:28]
 **/
public class SafeMathTest {

    @Test
    public void getZero() {
        Object zero = SafeMath.getZero(BigDecimal.class);
        System.out.println(zero.toString());
    }

    @Test
    public void value() {
        Object value = SafeMath.value(Integer.class, "1.2299");
        System.out.println(value.toString());
    }

    @Test
    public void add() {
        Object value = SafeMath.add("100", 2, Long.class);
        System.out.println(value.toString());
    }

    @Test
    public void scale() {
        Object value = SafeMath.scale(100.9988, 2, Double.class);
        System.out.println(value.toString());
    }

}
