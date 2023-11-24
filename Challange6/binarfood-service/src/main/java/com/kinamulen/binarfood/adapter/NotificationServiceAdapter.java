package com.kinamulen.binarfood.adapter;

import com.kinamulen.binarfood.adapter.request.NotificationOtpWebRequest;
import com.kinamulen.binarfood.adapter.request.NotificationWebRequest;
import com.kinamulen.binarfood.adapter.response.NotificationWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationServiceAdapter {

    @Autowired
    private RestTemplate restTemplate;

    public NotificationWebResponse sendEmail(NotificationWebRequest request) {
        String url = "http://NOTIFICATION-SERVICE/notification";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NotificationWebRequest> requestEntity = new HttpEntity<>(request,headers);
        return restTemplate.postForObject(url, requestEntity, NotificationWebResponse.class);
    }

    public NotificationWebResponse sendOtp(NotificationOtpWebRequest request) {
        String url = "http://NOTIFICATION-SERVICE/notification/otp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NotificationOtpWebRequest> requestEntity = new HttpEntity<>(request,headers);
        return restTemplate.postForObject(url, requestEntity, NotificationWebResponse.class);
    }
}
