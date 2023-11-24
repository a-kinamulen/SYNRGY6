package com.kinamulen.binarfood.adapter.request;

import com.kinamulen.binarfood.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCredentialWebRequest {
    private String username;
    private String password;
    private UUID binarfoodId;
    private UserType type;
}