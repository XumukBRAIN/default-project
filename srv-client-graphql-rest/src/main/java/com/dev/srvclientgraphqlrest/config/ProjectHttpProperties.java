package com.dev.srvclientgraphqlrest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ConfigurationProperties(prefix = "http-item-service")
@Component
public class ProjectHttpProperties {

    private String httpMethod;
    private URI httpUrl;
    private MediaType mediaType = MediaType.APPLICATION_JSON;
    private HttpSettings settings;

    @Getter
    @Setter
    public static class HttpSettings {
        private Integer httpCallTimeoutSeconds;
        private Integer httpConnectTimeoutSeconds;
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration connectTimeout;
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration callTimeout;
        private boolean enabledHttp2 = false;

        public Duration getConnectTimeout() {
            return connectTimeout == null ? Duration.ofSeconds(httpConnectTimeoutSeconds) : connectTimeout;
        }

        public Duration getCallTimeout () {
            return callTimeout == null ? Duration.ofSeconds(httpCallTimeoutSeconds) : callTimeout;
        }
    }
}
