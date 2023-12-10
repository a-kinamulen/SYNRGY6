package com.kinamulen.binarfood.stream.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUpdatePasswordMessage {
    private UUID binarfoodId;
    private String newPassword;
}
