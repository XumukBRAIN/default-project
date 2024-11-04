package com.dev.srvclientgraphqlrest.service;

import com.dev.srvclientgraphqlrest.config.ProjectHttpProperties;
import com.dev.srvclientgraphqlrest.converter.Converter;
import com.dev.srvclientgraphqlrest.model.GetItemRequest;
import com.dev.srvclientgraphqlrest.model.Item;
import com.dev.srvclientgraphqlrest.publishers.DefaultEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class IntegrationService {

    private final ObjectMapper objectMapper;

    private final ProjectHttpProperties httpProperties;

    private final HttpClient httpClient;

    private final Converter converter;

    private final DefaultEventPublisher eventPublisher;

    public IntegrationService(ObjectMapper objectMapper, @Qualifier("projectHttpProperties") ProjectHttpProperties httpProperties, HttpClient httpClient, Converter converter, DefaultEventPublisher eventPublisher) {
        this.objectMapper = objectMapper;
        this.httpProperties = httpProperties;
        this.httpClient = httpClient;
        this.converter = converter;
        this.eventPublisher = eventPublisher;
    }

    @SchemaMapping
    public Item getItem(Integer itemId) {
        log.info("srv-back-graphql-rest: Getting future item with id {}", itemId);
        GetItemRequest getItemRequest = converter.buildGetItemRequest(itemId);
        try {
            String stringObject = objectMapper.writeValueAsString(getItemRequest);
            log.info("srv-back-graphql-rest: GetItemRequest: {}", stringObject);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(stringObject);
            HttpRequest.Builder request = converter.buildHttpRequest(body, httpProperties);
            HttpResponse<String> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            log.info("srv-back-graphql-rest: Response body: {}", responseBody);
            eventPublisher.publish(responseBody);
            return objectMapper.readValue(responseBody, Item.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
