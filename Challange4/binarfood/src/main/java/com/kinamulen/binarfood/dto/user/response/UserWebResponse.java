package com.kinamulen.binarfood.dto.user.response;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWebResponse {
    private UUID id;
    private String username;
    private String phoneNumber;
    private String emailAddress;
}
