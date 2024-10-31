package com.dev.srvbackgrpckafka.service;

import com.dev.grpc.item.GetItemService;
import com.dev.grpc.item.ItemServiceGrpc;
import com.dev.srvbackgrpckafka.model.GetItemRequest;
import com.dev.srvbackgrpckafka.model.GetItemResponse;
import com.dev.srvbackgrpckafka.util.ResponseListener;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class GetItemGrpcService extends ItemServiceGrpc.ItemServiceImplBase {

    private static final String FROM_SYSTEM_NAME = "srv-back-grpc-kafka";

    private final GetItemKafkaProducer getItemKafkaProducer;

    @Override
    public void getItem(GetItemService.GetItemByIdRq request, StreamObserver<GetItemService.GetItemByIdRs> responseObserver) {
        try {
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .itemId(request.getItemId())
                    .reqId(request.getReqId())
                    .reqTm(request.getReqTm())
                    .fromSystemName(FROM_SYSTEM_NAME)
                    .build();

            getItemKafkaProducer.sendGetItemRequest(getItemRequest);

            GetItemResponse item = waitForResponse(getItemRequest.reqId());

            GetItemService.GetItemByIdRs response = GetItemService.GetItemByIdRs.newBuilder()
                    .setItem(GetItemService.Item.newBuilder()
                            .setTitle(item.getTitle())
                            .setDescription(item.getDescription())
                            .setPrice(item.getPrice().doubleValue())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка в работе метода getItem: " + e.getMessage());
        }
    }

    private GetItemResponse waitForResponse(String reqId) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        GetItemResponse[] responseHolder = new GetItemResponse[1];

        // Создаем слушатель для получения ответа
        ResponseListener listener = new ResponseListener(latch, responseHolder, reqId);
        listener.startListening(); // Запускаем слушателя

        // Ожидание ответа с таймаутом (например, 10 секунд)
        boolean await = latch.await(100, TimeUnit.SECONDS);

        if (!await || responseHolder[0] == null) {
            throw new RuntimeException("Timeout waiting for response");
        }

        return responseHolder[0];
    }
}
