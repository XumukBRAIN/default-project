package com.dev.srvclientgraphqlrest.listeners;

import com.dev.srvclientgraphqlrest.events.DefaultEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventListener {

    @EventListener
    public void handleDefaultEvent(DefaultEvent event) {
        System.out.println("Event Received: " + event.getMessage());
    }
}
