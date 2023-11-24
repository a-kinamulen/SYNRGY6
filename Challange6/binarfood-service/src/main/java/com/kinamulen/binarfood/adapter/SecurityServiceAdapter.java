package com.kinamulen.binarfood.adapter;

import com.kinamulen.binarfood.adapter.request.CreateUserCredentialWebRequest;
import com.kinamulen.binarfood.adapter.request.NotificationWebRequest;
import com.kinamulen.binarfood.adapter.request.UpdatePasswordWebRequest;
import com.kinamulen.binarfood.adapter.response.CreateUserCredentialWebResponse;
import com.kinamulen.binarfood.adapter.response.UpdatePasswordWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SecurityServiceAdapter {

    @Autowired
    private RestTemplate restTemplate;

    public CreateUserCredentialWebResponse addNewUser(CreateUserCredentialWebRequest request) {
        String url = "http://SECURITY-SERVICE/auth/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserCredentialWebRequest> requestEntity = new HttpEntity<>(request,headers);
        CreateUserCredentialWebResponse response = restTemplate.postForObject(url, requestEntity, CreateUserCredentialWebResponse.class);
        return response;
    }

    public UpdatePasswordWebResponse updatePassword(UpdatePasswordWebRequest request) {
        String url = "http://SECURITY-SERVICE/auth/update-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdatePasswordWebRequest> requestEntity = new HttpEntity<>(request,headers);
        UpdatePasswordWebResponse response = restTemplate.postForObject(url, requestEntity, UpdatePasswordWebResponse.class);
        return response;
    }
}
