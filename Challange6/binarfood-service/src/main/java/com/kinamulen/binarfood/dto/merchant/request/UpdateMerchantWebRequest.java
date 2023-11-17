package com.kinamulen.binarfood.dto.merchant.request;

import com.kinamulen.binarfood.enums.MerchantType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMerchantWebRequest {
    private String merchantName;
    private Boolean open;
    private String phoneNumber;
    private String merchantLocation;
    private MerchantType type;
}
