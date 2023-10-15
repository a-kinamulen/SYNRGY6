package com.kinamulen.binarfood.dto.merchant.request;

import com.kinamulen.binarfood.enums.MerchantType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMerchantWebRequest {
    private String merchantName;
    private String phoneNumber;
    private String merchantLocation;
    private MerchantType type;
}
