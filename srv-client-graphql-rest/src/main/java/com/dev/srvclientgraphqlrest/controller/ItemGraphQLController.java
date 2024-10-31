package com.dev.srvclientgraphqlrest.controller;

import com.dev.srvclientgraphqlrest.model.Item;
import com.dev.srvclientgraphqlrest.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemGraphQLController {

    private final IntegrationService integrationService;

    @QueryMapping("getItemById")
    public Item getOrder(@Argument Integer id) {
        return integrationService.getItem(id);
    }
}
