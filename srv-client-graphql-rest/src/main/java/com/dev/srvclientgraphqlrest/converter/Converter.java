package com.dev.srvclientgraphqlrest.converter;

import com.dev.srvclientgraphqlrest.config.ProjectHttpProperties;
import com.dev.srvclientgraphqlrest.model.GetItemRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class Converter {

    private final static String FROM_SYSTEM_NAME = "srv-client-graphql-rest";

    public GetItemRequest buildGetItemRequest(Integer itemId) {
        return GetItemRequest.builder()
                .itemId(itemId)
                .reqId(UUID.randomUUID().toString())
                .reqTm(String.valueOf(LocalDateTime.now()))
                .fromSystemName(FROM_SYSTEM_NAME)
                .build();
    }

    public HttpRequest.Builder buildHttpRequest(HttpRequest.BodyPublisher body, ProjectHttpProperties properties) {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        String method = properties.getHttpMethod();
        if (StringUtils.hasText(method) || method.equals("POST")) {
            builder.POST(body);
        } else {
            builder.method(method, body);
        }

        if (body.contentLength() != 0) {
            builder.setHeader(HttpHeaders.CONTENT_TYPE, properties.getMediaType().toString());
        }

        builder.timeout(properties.getSettings().getCallTimeout());
        builder.uri(properties.getHttpUrl());
        return builder;
    }
}
