package org.kinamulen.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Format {
    public static String padRight(String s, int n){
        return String.format("%-" + n + "s", s);
    }
    public static String padLeft(String s, int n){
        return String.format("%" + n + "s", s);
    }
    public static String formatRupiah(int number) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat customFormat = (DecimalFormat)nf;
        return customFormat.format(number);
    }
    private Format() {
        throw new IllegalStateException("Utility class");
    }
}
