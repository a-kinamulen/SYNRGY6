package com.kinamulen.binarfood.dto.merchant.response;

import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.enums.MerchantType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMerchantWebResponse {
    private UUID id;
    private String merchantName;
    private Boolean open;
    private String phoneNumber;
    private String merchantLocation;
    private MerchantType type;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private WalletWebResponse walletWebResponse;
}
