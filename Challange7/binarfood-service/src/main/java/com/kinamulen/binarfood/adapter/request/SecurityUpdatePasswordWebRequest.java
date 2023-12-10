package com.kinamulen.binarfood.adapter.request;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUpdatePasswordWebRequest {
    private UUID binarfoodId;
    private String newPassword;
}
