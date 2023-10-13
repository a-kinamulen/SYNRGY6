package com.kinamulen.binarfood.dto.wallet.response;

import com.kinamulen.binarfood.enums.WalletType;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletWebResponse {
    private UUID id;
    private Double balance;
    private WalletType type;
}
