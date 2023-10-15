package com.kinamulen.binarfood.dto.product.response;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWebResponse {
    private UUID id;
    private String productName;
    private Double price;
    private String merchantName;
}
