package com.kinamulen.binarfood.dto.merchant.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantReportWebRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
