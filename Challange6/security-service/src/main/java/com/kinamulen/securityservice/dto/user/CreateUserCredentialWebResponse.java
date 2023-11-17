package com.kinamulen.securityservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserCredentialWebResponse {
    private String token;
    private String username;
    private UUID binarfoodId;
}
