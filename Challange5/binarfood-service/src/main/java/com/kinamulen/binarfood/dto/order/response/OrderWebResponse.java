package com.kinamulen.binarfood.dto.order.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Double totalPrice; //sum of OrderDetail.totalPrice
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderDetailWebResponse> orderDetailWebResponses;
}
