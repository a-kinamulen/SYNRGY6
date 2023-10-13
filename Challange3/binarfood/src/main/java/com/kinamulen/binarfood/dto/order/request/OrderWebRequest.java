package com.kinamulen.binarfood.dto.order.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    OrderWebRequest {
    private List<OrderDetailWebRequest> products;
    private String destinationAddress;
}
