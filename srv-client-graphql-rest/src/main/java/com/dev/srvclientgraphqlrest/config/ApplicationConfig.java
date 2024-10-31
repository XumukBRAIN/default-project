package com.dev.srvclientgraphqlrest.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.CookieManager;
import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(ProjectHttpProperties.class)
public class ApplicationConfig {

    @Bean
    public HttpClient httpClient(@Qualifier("projectHttpProperties") ProjectHttpProperties properties) {
        ProjectHttpProperties.HttpSettings settings = properties.getSettings();
        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(settings.getConnectTimeout())
                .cookieHandler(new CookieManager());

        if (settings.isEnabledHttp2()) {
            builder.version(HttpClient.Version.HTTP_2);
        } else {
            builder.version(HttpClient.Version.HTTP_1_1);
        }

        return builder.build();
    }
}
