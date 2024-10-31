package com.dev.srvbackrestgrpc.controller;

import com.dev.srvbackrestgrpc.model.GetItemRequest;
import com.dev.srvbackrestgrpc.model.GetItemResponse;
import com.dev.srvbackrestgrpc.service.IntegrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItemController {

    private final ObjectMapper objectMapper;

    private final IntegrationService service;

    @PostMapping("/getItemById")
    public ResponseEntity<String> getItemById(@RequestBody String request) {
        try {
            GetItemRequest getItemRequest = objectMapper.readValue(request, GetItemRequest.class);
            GetItemResponse item = service.getItem(getItemRequest);
            return ResponseEntity.ok().body(objectMapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
