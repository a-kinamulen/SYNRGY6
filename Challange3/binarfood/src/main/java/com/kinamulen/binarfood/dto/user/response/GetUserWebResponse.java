package com.kinamulen.binarfood.dto.user.response;

import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserWebResponse {
    private UUID id;
    private String username;
    private String phoneNumber;
    private String emailAddress;
    private WalletWebResponse walletWebResponse;
}
