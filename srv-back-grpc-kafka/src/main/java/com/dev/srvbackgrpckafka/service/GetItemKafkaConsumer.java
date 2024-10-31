package com.dev.srvbackgrpckafka.service;

import com.dev.srvbackgrpckafka.model.GetItemResponse;
import com.dev.srvbackgrpckafka.util.GetItemResponseStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetItemKafkaConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "getItemByIdRs", groupId = "getItemResponseConsumer")
    public void listen(String message) {
        try {
            GetItemResponse getItemResponse = objectMapper.readValue(message, GetItemResponse.class);
            String reqId = getItemResponse.getReqId();
            GetItemResponseStore.saveResponse(reqId, getItemResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
