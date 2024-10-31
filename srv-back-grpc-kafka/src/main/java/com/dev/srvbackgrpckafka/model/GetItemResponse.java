package com.dev.srvbackgrpckafka.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetItemResponse {

    private String title;

    private String description;

    private BigDecimal price;

    private String reqId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetItemResponse getItemResponse)) return false;
        return Objects.equals(title, getItemResponse.title) && Objects.equals(description, getItemResponse.description) && Objects.equals(price, getItemResponse.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, price);
    }
}
