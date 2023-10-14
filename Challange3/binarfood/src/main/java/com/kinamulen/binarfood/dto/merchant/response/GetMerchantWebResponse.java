package com.kinamulen.binarfood.dto.merchant.response;

import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.enums.MerchantType;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMerchantWebResponse {
    private UUID id;
    private String merchantName;
    private String phoneNumber;
    private String merchantLocation;
    private MerchantType type;
    private WalletWebResponse walletWebResponse;
}
