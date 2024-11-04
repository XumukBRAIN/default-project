package com.dev.srvbackgrpckafka.util;

import com.dev.srvbackgrpckafka.model.GetItemResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GetItemResponseStore {

    private static final Map<String, GetItemResponse> GET_ITEM_RESPONSE_STORE = new ConcurrentHashMap<>();

    public static void saveResponse(String reqId, GetItemResponse itemResponse) {
        GET_ITEM_RESPONSE_STORE.put(reqId, itemResponse);
    }

    public static GetItemResponse getResponse(String reqId) {
        return GET_ITEM_RESPONSE_STORE.get(reqId);
    }

    public static void removeResponse(String reqId) {
        GET_ITEM_RESPONSE_STORE.remove(reqId);
    }
}
