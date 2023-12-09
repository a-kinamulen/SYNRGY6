package com.kinamulen.notificationservice.controller;

import com.kinamulen.notificationservice.dto.NotificationOtpWebRequest;
import com.kinamulen.notificationservice.dto.NotificationWebRequest;
import com.kinamulen.notificationservice.dto.NotificationWebResponse;
import com.kinamulen.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationWebResponse sendEmailWithAttachment(@RequestBody NotificationWebRequest request) {
        notificationService.sendSimpleEmail(request);
        return NotificationWebResponse.builder()
                .response("Sent email via notification service :D")
                .build();
    }

    @PostMapping(value = "/otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationWebResponse sendOtpViaEmail(@RequestBody NotificationOtpWebRequest request) {
        notificationService.sendOtpViaEmail(request);
        return NotificationWebResponse.builder()
                .response("Sent OTP via email via notification service :D")
                .build();
    }
}
