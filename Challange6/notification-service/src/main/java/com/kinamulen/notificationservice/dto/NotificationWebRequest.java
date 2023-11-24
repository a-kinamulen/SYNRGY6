package com.kinamulen.notificationservice.dto;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationWebRequest {
    private String receiverEmail;
    private String pdfByte;
}
