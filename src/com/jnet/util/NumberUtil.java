package com.jnet.util;

import java.text.DecimalFormat;

/**
 * @author Xunwu Yang 2020-12-31
 * @version 1.0.0
 */
public class NumberUtil {

    private static final String FORMAT = "###################.00";

    private static DecimalFormat decimalFormat = new DecimalFormat(FORMAT);

    public static String format(Number number) {
        return decimalFormat.format(number);
    }

    public static void main(String[] args) {
        System.out.println(format(12.334));
    }
}
