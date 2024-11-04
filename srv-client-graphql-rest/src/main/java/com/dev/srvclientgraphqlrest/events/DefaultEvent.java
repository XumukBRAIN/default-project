package com.dev.srvclientgraphqlrest.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DefaultEvent extends ApplicationEvent {

    private final String message;

    public DefaultEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    @Override
    public String toString() {
        return "DefaultEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
