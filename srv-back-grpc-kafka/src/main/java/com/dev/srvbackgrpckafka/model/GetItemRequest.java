package com.dev.srvbackgrpckafka.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record GetItemRequest(Integer itemId, String reqId, String reqTm, String fromSystemName) {
}
