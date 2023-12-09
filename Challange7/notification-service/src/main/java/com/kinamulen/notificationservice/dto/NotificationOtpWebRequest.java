package com.kinamulen.notificationservice.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationOtpWebRequest {
    private String receiverEmail;
    private String otp;
}
