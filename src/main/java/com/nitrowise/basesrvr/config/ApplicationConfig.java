package com.nitrowise.basesrvr.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static com.nitrowise.basesrvr.util.FormatterUtils.getDefaultDateFormatter;
import static com.nitrowise.basesrvr.util.FormatterUtils.getDefaultDateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    ObjectMapper getObjectMapper() {
        ObjectMapper mapper = getJackson2ObjectMapperBuilder().build();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    private Jackson2ObjectMapperBuilder getJackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializers(
                        new LocalDateTimeSerializer(getDefaultDateTimeFormatter()),
                        new LocalDateSerializer(getDefaultDateFormatter())
                )
                .deserializers(
                        new LocalDateTimeDeserializer(getDefaultDateTimeFormatter()),
                        new LocalDateDeserializer(getDefaultDateFormatter())
                );
    }

    @Bean
    MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter(@Autowired ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}
