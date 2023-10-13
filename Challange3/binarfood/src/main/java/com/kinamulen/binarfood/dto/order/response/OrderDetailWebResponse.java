package com.kinamulen.binarfood.dto.order.response;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailWebResponse {
    private UUID id;
    private Integer quantity;
    private Double totalPrice;
}
