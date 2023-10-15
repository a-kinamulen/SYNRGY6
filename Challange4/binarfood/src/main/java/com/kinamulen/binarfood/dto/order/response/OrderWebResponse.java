package com.kinamulen.binarfood.dto.order.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderWebResponse {
    private UUID id; //id order
    private LocalDateTime orderTime;
    private String destinationAddress;
    private Boolean completed;
    private Double totalPrice; //sum of OrderDetailWebResponse.totalPrice

    private List<OrderDetailWebResponse> orderDetailWebResponses;
}
