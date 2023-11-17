package com.kinamulen.binarfood.adapter;

import com.kinamulen.binarfood.adapter.request.CreateUserCredentialWebRequest;
import com.kinamulen.binarfood.adapter.response.CreateUserCredentialWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SecurityServiceAdapter {

    @Autowired
    private RestTemplate restTemplate;

    public CreateUserCredentialWebResponse addNewUser(CreateUserCredentialWebRequest request) {
        String url = "http://SECURITY-SERVICE/auth/register";
        CreateUserCredentialWebResponse response = restTemplate.postForObject(url, request, CreateUserCredentialWebResponse.class);
        return response;
    }
}
