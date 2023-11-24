package com.kinamulen.securityservice.controller;

import com.kinamulen.securityservice.dto.auth.AuthRequest;
import com.kinamulen.securityservice.dto.auth.UpdatePasswordWebRequest;
import com.kinamulen.securityservice.dto.auth.UpdatePasswordWebResponse;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebRequest;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebResponse;
import com.kinamulen.securityservice.entity.UserCredential;
import com.kinamulen.securityservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateUserCredentialWebResponse addNewUser(@RequestBody CreateUserCredentialWebRequest request) {
        return service.saveUser(request);
    }
    @PostMapping(value = "/update-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UpdatePasswordWebResponse updatePassword(@RequestBody UpdatePasswordWebRequest request) {
        return service.updatePassword(request);
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getToken(@RequestBody AuthRequest authRequest) {
        return service.generateToken(authRequest);
    }

    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean validateToken(@RequestParam("token") String token) {
        return service.validateToken(token);
    }

}
