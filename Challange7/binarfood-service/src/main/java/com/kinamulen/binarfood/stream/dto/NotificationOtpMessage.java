package com.kinamulen.binarfood.stream.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationOtpMessage {
    private String receiverEmail;
    private String otp;
}
