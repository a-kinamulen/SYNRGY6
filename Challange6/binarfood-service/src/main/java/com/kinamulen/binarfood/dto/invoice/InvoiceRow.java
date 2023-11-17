package com.kinamulen.binarfood.dto.invoice;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRow {
    private String merchantName;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
}
