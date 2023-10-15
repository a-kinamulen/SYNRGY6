package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.user.request.RegisterUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.UpdateUserWebRequest;
import com.kinamulen.binarfood.dto.user.response.GetUserWebResponse;
import com.kinamulen.binarfood.dto.user.response.UserWebResponse;
import com.kinamulen.binarfood.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserWebResponse> register(@RequestBody RegisterUserWebRequest request){
        UserWebResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserWebResponse>> getUsers() {
        List<UserWebResponse> responses = userService.getUsers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserWebResponse> getUser(@PathVariable UUID id) {
        GetUserWebResponse response = userService.getUser(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWebResponse> update(
            @PathVariable UUID id, @RequestBody UpdateUserWebRequest updateUserWebRequest) {
        UserWebResponse response = userService.updateUser(id, updateUserWebRequest);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        //to do: pake field isDeleted
        boolean response = userService.deleteUser(id);
        if (response) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

}
