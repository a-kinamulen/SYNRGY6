package com.kinamulen.binarfood.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRow {
    private LocalDateTime orderTime;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
