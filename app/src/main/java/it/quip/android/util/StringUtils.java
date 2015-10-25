package it.quip.android.util;

public class StringUtils {

    public static boolean isBlank(String s) {
        return s == null || "".equals(s);
    }

}
