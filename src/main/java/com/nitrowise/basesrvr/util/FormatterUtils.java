package com.nitrowise.basesrvr.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class FormatterUtils {

    private FormatterUtils() {
    }

    public static DateTimeFormatter getDefaultDateTimeFormatter() {
        String format = "yyyy-MM-dd";
        return new DateTimeFormatterBuilder()
                .appendPattern(format)
                .toFormatter();
    }

    public static DateTimeFormatter getDefaultDateFormatter() {
        String format = "yyyy-MM-dd HH:mm:SS";
        return new DateTimeFormatterBuilder()
                .appendPattern(format)
                .toFormatter();
    }
}
