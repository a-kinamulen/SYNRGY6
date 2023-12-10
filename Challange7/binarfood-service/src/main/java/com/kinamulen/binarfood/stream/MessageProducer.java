package com.kinamulen.binarfood.stream;

import com.google.gson.Gson;
import com.kinamulen.binarfood.stream.dto.NotificationOtpMessage;
import com.kinamulen.binarfood.stream.dto.SecurityUpdatePasswordMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String SEND_NOTIFICATION_OTP_TOPIC = "send-notification-otp-topic";
    private final String SECURITY_UPDATE_PASSWORD_TOPIC = "security-update-password-topic";

    private void sendMessage(String topic, String message){
        kafkaTemplate.send(topic, message);
    }

    public void sendMessageOtp(NotificationOtpMessage message) {
        Gson gson = new Gson();
        sendMessage(SEND_NOTIFICATION_OTP_TOPIC, gson.toJson(message));
    }

    public void sendMessageUpdatePassword(SecurityUpdatePasswordMessage message) {
        Gson gson = new Gson();
        sendMessage(SECURITY_UPDATE_PASSWORD_TOPIC, gson.toJson(message));
    }
}
