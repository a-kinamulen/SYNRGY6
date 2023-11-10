package com.kinamulen.securityservice.controller;

import com.kinamulen.securityservice.dto.auth.AuthRequest;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebRequest;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebResponse;
import com.kinamulen.securityservice.entity.UserCredential;
import com.kinamulen.securityservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public CreateUserCredentialWebResponse addNewUser(@RequestBody CreateUserCredentialWebRequest request) {
        return service.saveUser(request);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        return service.generateToken(authRequest);
    }

    @GetMapping("/validate")
    public Boolean validateToken(@RequestParam("token") String token) {
        return service.validateToken(token);
    }
}
