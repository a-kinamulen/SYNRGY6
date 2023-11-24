package com.kinamulen.binarfood.dto.wallet.response;

import com.kinamulen.binarfood.enums.UserType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletWebResponse {
    private UUID id;
    private Double balance;
    private UserType type;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
