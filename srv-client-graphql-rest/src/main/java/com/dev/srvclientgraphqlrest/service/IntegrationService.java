package com.dev.srvclientgraphqlrest.service;

import com.dev.srvclientgraphqlrest.config.ProjectHttpProperties;
import com.dev.srvclientgraphqlrest.converter.Converter;
import com.dev.srvclientgraphqlrest.model.GetItemRequest;
import com.dev.srvclientgraphqlrest.model.Item;
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

    public IntegrationService(ObjectMapper objectMapper, @Qualifier("projectHttpProperties") ProjectHttpProperties httpProperties, HttpClient httpClient, Converter converter) {
        this.objectMapper = objectMapper;
        this.httpProperties = httpProperties;
        this.httpClient = httpClient;
        this.converter = converter;
    }

    @SchemaMapping
    public Item getItem(Integer itemId) {
        GetItemRequest getItemRequest = converter.buildGetItemRequest(itemId);
        try {
            String stringObject = objectMapper.writeValueAsString(getItemRequest);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(stringObject);
            HttpRequest.Builder request = converter.buildHttpRequest(body, httpProperties);
            HttpResponse<String> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), Item.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
