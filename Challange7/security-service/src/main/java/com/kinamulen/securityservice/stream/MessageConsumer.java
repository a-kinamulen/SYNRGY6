package com.kinamulen.securityservice.stream;

import com.google.gson.Gson;
import com.kinamulen.securityservice.service.AuthService;
import com.kinamulen.securityservice.stream.dto.SecurityUpdatePasswordMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    private AuthService authService;

    private final String SECURITY_UPDATE_PASSWORD_TOPIC = "security-update-password-topic";

    @KafkaListener(topics=SECURITY_UPDATE_PASSWORD_TOPIC, groupId = "binarfood")
    public void listen(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        Gson gson = new Gson();
        SecurityUpdatePasswordMessage securityUpdatePasswordMessage = gson.fromJson(message, SecurityUpdatePasswordMessage.class);
        authService.updatePassword(securityUpdatePasswordMessage);
    }
}
