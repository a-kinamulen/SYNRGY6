package com.kinamulen.securityservice.service;

import com.kinamulen.securityservice.dto.auth.AuthRequest;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebRequest;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebResponse;
import com.kinamulen.securityservice.entity.UserCredential;
import com.kinamulen.securityservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public CreateUserCredentialWebResponse saveUser(CreateUserCredentialWebRequest request) {
        UserCredential userCredential = UserCredential.builder()
                .username(request.getUsername())
                .password(Objects.nonNull(request.getPassword()) ? passwordEncoder.encode(request.getPassword()) : "null")
                .binarfoodId(request.getBinarfoodId())
                .build();

        userCredential = userCredentialRepository.save(userCredential);
        String token = jwtService.generateToken(request.getUsername());
        return toWebResponse(userCredential,token);
    }


    public String generateToken(AuthRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUsername());
            } else return "Bad credentials";
        } catch (BadCredentialsException e) {
            return e.getMessage();
        }
    }

    public Boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    private CreateUserCredentialWebResponse toWebResponse(UserCredential userCredential, String token) {
        return CreateUserCredentialWebResponse.builder()
                .token(token)
                .username(userCredential.getUsername())
                .binarfoodId(userCredential.getBinarfoodId())
                .build();
    }

}
