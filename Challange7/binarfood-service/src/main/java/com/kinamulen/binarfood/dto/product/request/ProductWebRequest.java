package com.kinamulen.binarfood.dto.product.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWebRequest {
    private String productName;
    private Double price;
}
