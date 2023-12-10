package com.kinamulen.notificationservice.stream;

import com.google.gson.Gson;
import com.kinamulen.notificationservice.service.NotificationService;
import com.kinamulen.notificationservice.stream.dto.NotificationOtpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    private NotificationService notificationService;

    private final String SEND_NOTIFICATION_OTP_TOPIC = "send-notification-otp-topic";

    @KafkaListener(topics=SEND_NOTIFICATION_OTP_TOPIC, groupId = "binarfood")
    public void listen(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        Gson gson = new Gson();
        NotificationOtpMessage notificationOtpMessage = gson.fromJson(message, NotificationOtpMessage.class);
        notificationService.sendOtpViaEmail(notificationOtpMessage);
    }
}
