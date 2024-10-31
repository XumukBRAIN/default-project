package com.dev.srvbackgrpckafka.service;

import com.dev.srvbackgrpckafka.model.GetItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetItemKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendGetItemRequest(GetItemRequest request) {
        try {
            String rqStr = objectMapper.writeValueAsString(request);
            kafkaTemplate.send("getItemByIdRq", rqStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
