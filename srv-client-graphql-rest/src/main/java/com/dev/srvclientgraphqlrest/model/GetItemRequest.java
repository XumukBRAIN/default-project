package com.dev.srvclientgraphqlrest.model;

import lombok.*;

@Builder(toBuilder = true)
public record GetItemRequest(Integer itemId, String reqId, String reqTm, String fromSystemName) {
}
