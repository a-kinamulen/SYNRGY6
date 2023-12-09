package com.kinamulen.binarfood.adapter.request;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationWebRequest {
    private String receiverEmail;
    private String pdfByte;
}
