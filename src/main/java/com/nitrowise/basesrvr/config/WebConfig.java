package com.nitrowise.basesrvr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = null;
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter ) {
                MappingJackson2HttpMessageConverter jacksonConverter =
                        ((MappingJackson2HttpMessageConverter) converter);

                if (objectMapper == null) {
                    objectMapper = jacksonConverter.getObjectMapper();
                } else {
                    jacksonConverter.setObjectMapper(objectMapper);
                }
            }
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .maxAge(3600)
                .allowCredentials(true)
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Content-Length", "Content-Transfer-Encoding", "Accept", "if-modified-since", "Cache-Control", "Pragma", "Location", "Response-Header", "X-Powered-By", "TOKEN", "token", "user_id")
                .exposedHeaders("Origin", "X-Requested-With", "Content-Type", "Content-Length", "Content-Transfer-Encoding", "Accept", "if-modified-since", "Cache-Control", "Pragma", "Location", "Response-Header", "X-Powered-By", "TOKEN", "token", "user_id")
                .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PUT");
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
