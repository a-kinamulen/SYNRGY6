package com.kinamulen.binarfood.dto.user.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserWebRequest {
    private String username;
    private String password;
    private String phoneNumber;
    private String emailAddress;
}
