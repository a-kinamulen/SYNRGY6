package com.kinamulen.securityservice.service;

import com.kinamulen.securityservice.dto.auth.AuthRequest;
import com.kinamulen.securityservice.dto.auth.UpdatePasswordWebRequest;
import com.kinamulen.securityservice.dto.auth.UpdatePasswordWebResponse;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebRequest;
import com.kinamulen.securityservice.dto.user.CreateUserCredentialWebResponse;
import com.kinamulen.securityservice.entity.UserCredential;
import com.kinamulen.securityservice.repository.UserCredentialRepository;
import com.kinamulen.securityservice.stream.dto.SecurityUpdatePasswordMessage;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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

    @Autowired
    private UserCredentialRepository repository;

    public CreateUserCredentialWebResponse saveUser(CreateUserCredentialWebRequest request) {
        UserCredential userCredential = UserCredential.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .binarfoodId(request.getBinarfoodId())
                .type(request.getType())
                .build();

        userCredential = userCredentialRepository.save(userCredential);
        String token = jwtService.generateToken(request.getUsername(), request.getBinarfoodId(), request.getType());
        return toWebResponse(userCredential,token);
    }
    public UpdatePasswordWebResponse updatePassword(UpdatePasswordWebRequest request) {
        Optional<UserCredential> userCredential = repository.findByBinarfoodId(request.getBinarfoodId());
        if (userCredential.isPresent()) {
            userCredential.get().setPassword(request.getNewPassword());
            userCredentialRepository.save(userCredential.get());
            return UpdatePasswordWebResponse.builder().response("Password updated").build();
        }
        return null;
    }

    public UpdatePasswordWebResponse updatePassword(SecurityUpdatePasswordMessage message) {
        Optional<UserCredential> userCredential = repository.findByBinarfoodId(message.getBinarfoodId());
        if (userCredential.isPresent()) {
            userCredential.get().setPassword(message.getNewPassword());
            userCredentialRepository.save(userCredential.get());
            return UpdatePasswordWebResponse.builder().response("Password updated").build();
        }
        return null;
    }

    public String generateToken(AuthRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                Optional<UserCredential> credential = repository.findByUsername(authRequest.getUsername());
                return jwtService.generateToken(authRequest.getUsername(),
                        credential.get().getBinarfoodId(),
                        credential.get().getType());
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
