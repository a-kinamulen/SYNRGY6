package com.kinamulen.binarfood.dto.wallet.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletWebRequest {
    private Double amount;
}
