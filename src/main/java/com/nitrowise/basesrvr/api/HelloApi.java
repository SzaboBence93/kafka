package com.nitrowise.basesrvr.api;

import com.nitrowise.basesrvr.model.dto.LombokTestDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/hello")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloApi {

    @GetMapping("/")
    public HelloDto index() {
        return new HelloDto("Greetings from Spring Boot!");
    }

    @GetMapping("/lombok")
    public LombokTestDto getLombokTestDto() {
        return new LombokTestDto(22L, "name");
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class HelloDto {
        String message;
    }
    
}
