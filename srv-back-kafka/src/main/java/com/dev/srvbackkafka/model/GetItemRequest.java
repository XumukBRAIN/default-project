package com.dev.srvbackkafka.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record GetItemRequest(Integer itemId, String reqId, String reqTm, String fromSystemName) {
}
