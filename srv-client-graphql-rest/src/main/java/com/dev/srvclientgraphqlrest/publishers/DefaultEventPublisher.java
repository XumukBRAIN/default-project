package com.dev.srvclientgraphqlrest.publishers;

import com.dev.srvclientgraphqlrest.events.DefaultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(String message) {
        DefaultEvent event = new DefaultEvent(this, message);
        publisher.publishEvent(event);
    }
}