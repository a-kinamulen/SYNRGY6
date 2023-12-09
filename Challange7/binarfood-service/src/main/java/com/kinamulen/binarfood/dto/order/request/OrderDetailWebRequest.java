package com.kinamulen.binarfood.dto.order.request;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailWebRequest {
    private UUID productId;
    private Integer quantity;
}
