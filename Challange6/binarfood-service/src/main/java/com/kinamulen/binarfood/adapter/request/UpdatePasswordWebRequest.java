package com.kinamulen.binarfood.adapter.request;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordWebRequest {
    private UUID binarfoodId;
    private String newPassword;
}
