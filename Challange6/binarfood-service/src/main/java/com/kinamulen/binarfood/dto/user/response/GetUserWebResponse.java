package com.kinamulen.binarfood.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<OrderWebResponse> orderWebResponses;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
