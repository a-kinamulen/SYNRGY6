package com.kinamulen.binarfood.adapter.request;

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
