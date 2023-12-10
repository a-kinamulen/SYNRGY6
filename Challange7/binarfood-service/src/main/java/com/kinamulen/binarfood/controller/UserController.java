package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.user.request.ForgetPasswordWebRequest;
import com.kinamulen.binarfood.dto.user.request.RegisterUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.UpdateUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.VerifyUserWebRequest;
import com.kinamulen.binarfood.dto.user.response.ForgetPasswordWebResponse;
import com.kinamulen.binarfood.dto.user.response.GetUserWebResponse;
import com.kinamulen.binarfood.dto.user.response.UserWebResponse;
import com.kinamulen.binarfood.service.UserService;
import com.kinamulen.binarfood.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/register/_public")
    public ResponseEntity<UserWebResponse> register(@RequestBody RegisterUserWebRequest request){
        log.info("Starting user register, username: {}", request.getUsername());
        UserWebResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify/_public")
    public ResponseEntity<UserWebResponse> verify(@RequestBody VerifyUserWebRequest request) {
        log.info("Starting user verification, userId: {}", request.getId());
        UserWebResponse response = userService.verify(request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/trigger/forget-password/_public")
    public ResponseEntity<ForgetPasswordWebResponse> forgetPasswordTrigger(@RequestBody ForgetPasswordWebRequest request) {
        log.info("Starting user forget Password, userId: {}", request.getId());
        ForgetPasswordWebResponse response = userService.forgetPasswordTrigger(request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/forget-password/_public")
    public ResponseEntity<UserWebResponse> forgetPassword(@RequestBody ForgetPasswordWebRequest request) {
        log.info("Starting user forget Password verification, userId: {}", request.getId());
        UserWebResponse response = userService.forgetPassword(request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping
    public ResponseEntity<List<UserWebResponse>> getUsers(
            @RequestHeader(value = "page", required = false, defaultValue="0") Integer page,
            @RequestHeader(value = "size", required = false, defaultValue="10") Integer size,
            @RequestHeader(value = "sortBy", required = false, defaultValue="createdAt") String sortBy,
            @RequestHeader(value = "direction", required = false, defaultValue="DESC") String direction) {
        List<UserWebResponse> responses = userService.getUsers(page, size, sortBy, direction);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/_user-secured")
    public ResponseEntity<GetUserWebResponse> getUser(
            @PathVariable UUID id,
            @RequestHeader(value = "userId") String idFromToken) {
        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        GetUserWebResponse response = userService.getUser(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/_user-secured")
    public ResponseEntity<UserWebResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateUserWebRequest updateUserWebRequest,
            @RequestHeader(value = "userId") String idFromToken) {
        log.info("Starting user update, username: {}", updateUserWebRequest.getUsername());
        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        UserWebResponse response = userService.updateUser(id, updateUserWebRequest);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/_user-secured")
    public ResponseEntity<Boolean> delete(
            @PathVariable UUID id,
            @RequestHeader(value = "userId") String idFromToken) {
        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        boolean response = userService.deleteUser(id);
        if (response) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

}
