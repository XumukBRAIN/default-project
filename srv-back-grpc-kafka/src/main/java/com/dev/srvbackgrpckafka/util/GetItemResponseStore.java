package com.dev.srvbackgrpckafka.util;

import com.dev.srvbackgrpckafka.model.GetItemResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GetItemResponseStore {

    public static Map<String, GetItemResponse> getItemResponseStore = new ConcurrentHashMap<>();

    public static void saveResponse(String reqId, GetItemResponse itemResponse) {
        getItemResponseStore.put(reqId, itemResponse);
    }

    public static GetItemResponse getResponse(String reqId) {
        return getItemResponseStore.get(reqId);
    }

    public static void removeResponse(String reqId) {
        getItemResponseStore.remove(reqId);
    }
}
