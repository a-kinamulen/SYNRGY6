package com.kinamulen.securityservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCredentialWebRequest {
    private String username;
    private String password;
    private UUID binarfoodId;
}