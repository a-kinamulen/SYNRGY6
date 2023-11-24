package com.kinamulen.binarfood.dto.user.request;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyUserWebRequest {
    private UUID id;
    private String otp;
}
