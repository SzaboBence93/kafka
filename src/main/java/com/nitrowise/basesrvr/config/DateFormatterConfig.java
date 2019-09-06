package com.nitrowise.basesrvr.config;

import com.nitrowise.basesrvr.util.FormatterUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Configuration
public class DateFormatterConfig {

    @Bean
    @Primary
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {

            @Override
            public LocalDate parse(String dateString, Locale locale) {
                return LocalDate.parse(dateString, FormatterUtils.getDefaultDateFormatter());
            }

            @Override
            public String print(LocalDate localDate, Locale locale) {
                return FormatterUtils.getDefaultDateFormatter().format(localDate);
            }
        };
    }

    @Bean
    @Primary
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<LocalDateTime>() {

            @Override
            public LocalDateTime parse(String datetimeString, Locale locale) {
                return LocalDateTime.parse(datetimeString, FormatterUtils.getDefaultDateTimeFormatter());
            }

            @Override
            public String print(LocalDateTime localDateTime, Locale locale) {
                return FormatterUtils.getDefaultDateTimeFormatter().format(localDateTime);
            }

        };
    }

}
