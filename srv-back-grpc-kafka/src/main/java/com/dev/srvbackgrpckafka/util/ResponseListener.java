package com.dev.srvbackgrpckafka.util;

import com.dev.srvbackgrpckafka.model.GetItemResponse;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class ResponseListener {

    private final CountDownLatch latch;
    private final GetItemResponse[] responseHolder;
    private final String reqId;

    public void startListening() {
        new Thread(() -> {
            try {
                // Ожидаем появления ответа в хранилище
                while (true) {
                    GetItemResponse itemResponse = GetItemResponseStore.getResponse(reqId);
                    if (itemResponse != null) {
                        responseHolder[0] = itemResponse; // Сохраняем ответ
                        latch.countDown(); // Уменьшаем счетчик до 0
                        break; // Выходим из цикла
                    }
                    Thread.sleep(100); // Небольшая задержка перед следующей проверкой
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                GetItemResponseStore.removeResponse(reqId);
            }
        }).start();
    }
}
