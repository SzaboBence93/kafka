package com.nitrowise.basesrvr.api;

import com.nitrowise.basesrvr.service.KafkaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaApi {

    KafkaService kafkaService;

    @PostMapping("/send")
    public void postMessage(@RequestParam String topicName, @RequestParam String msg) {
        kafkaService.postMessage(topicName, msg);
    }

}
