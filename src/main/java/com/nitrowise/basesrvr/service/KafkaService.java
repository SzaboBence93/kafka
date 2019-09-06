package com.nitrowise.basesrvr.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void postMessage(String topicName, String msg) {
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topicName, msg);
        SendResult<String, String> stringStringSendResult;
        try {
            stringStringSendResult = send.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        int a = 1;
        a++;

    }

}
