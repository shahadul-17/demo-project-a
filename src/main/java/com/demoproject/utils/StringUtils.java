package com.demoproject.utils;

public final class StringUtils {

    public static boolean isNull(String text) {
        return text == null;
    }

    public static boolean isEmpty(String text) {
        return "".equals(text);
    }

    public static boolean isNullOrEmpty(String text) {
        return isNull(text) || isEmpty(text);
    }

    public static boolean isNullOrWhiteSpace(String text) {
        if (isNull(text)) { return true; }

        return isEmpty(text.trim());
    }
}
