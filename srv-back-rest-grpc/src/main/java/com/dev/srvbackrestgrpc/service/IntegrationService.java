package com.dev.srvbackrestgrpc.service;

import com.dev.grpc.item.GetItemService;
import com.dev.grpc.item.ItemServiceGrpc;
import com.dev.srvbackrestgrpc.model.GetItemRequest;
import com.dev.srvbackrestgrpc.model.GetItemResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class IntegrationService {

    @Value("${integration.grpc.targetService}")
    private String targetService;

    public GetItemResponse getItem(GetItemRequest getItemRequest) {
        log.info("srv-back-rest-grpc: getting request: {}", getItemRequest);
        ManagedChannel channel = ManagedChannelBuilder.forTarget(targetService)
                .usePlaintext()
                .build();

        ItemServiceGrpc.ItemServiceBlockingStub stub = ItemServiceGrpc.newBlockingStub(channel);

        GetItemService.GetItemByIdRq request = GetItemService.GetItemByIdRq.newBuilder()
                .setItemId(getItemRequest.itemId())
                .setReqId(getItemRequest.reqId())
                .setReqTm(getItemRequest.reqTm())
                .setFromSystemName(getItemRequest.fromSystemName())
                .build();

        GetItemService.GetItemByIdRs response = stub.getItem(request);
        log.info("srv-back-rest-grpc: getting response: {}", response);
        GetItemService.Item item = response.getItem();

        return GetItemResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .price(BigDecimal.valueOf(item.getPrice()))
                .build();
    }
}
