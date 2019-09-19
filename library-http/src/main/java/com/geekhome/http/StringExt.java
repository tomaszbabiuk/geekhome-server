package com.geekhome.http;

public class StringExt {
    public static String trimStart(String source, char toTrim) {
        while (source.length() > 0 && source.charAt(0) == toTrim) {
            source = source.substring(1);
        }
        return source;
    }

    public static String trimEnd(String source, String toTrim) {
        return source.replaceAll("^[" + toTrim + "]+", "");
    }
}
