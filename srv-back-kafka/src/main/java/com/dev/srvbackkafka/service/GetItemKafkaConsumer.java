package com.dev.srvbackkafka.service;

import com.dev.srvbackkafka.model.GetItemRequest;
import com.dev.srvbackkafka.model.GetItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//@Slf4j
@Service
@RequiredArgsConstructor
public class GetItemKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final GetItemKafkaProducer getItemKafkaProducer;

    @KafkaListener(topics = "getItemByIdRq", groupId = "getItemRequestConsumer")
    public void listen(String message) {
        try {
            GetItemRequest getItemRequest = objectMapper.readValue(message, GetItemRequest.class);

            GetItemResponse getItemResponse = GetItemResponse.builder()
                    .reqId(getItemRequest.reqId())
                    .title("Товар номер 1")
                    .description("Самый лучший товар")
                    .price(BigDecimal.valueOf(99.11))
                    .build();

            getItemKafkaProducer.sendGetItemRequest(getItemResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
