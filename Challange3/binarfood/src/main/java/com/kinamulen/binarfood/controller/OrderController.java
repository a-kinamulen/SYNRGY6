package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.dto.user.request.RegisterUserWebRequest;
import com.kinamulen.binarfood.dto.user.response.UserWebResponse;
import com.kinamulen.binarfood.service.OrderService;
import com.kinamulen.binarfood.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderDetailService;

    @PostMapping
    public ResponseEntity<OrderWebResponse> create(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody OrderWebRequest request){
        OrderWebResponse response = orderDetailService.create(username, password, request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/{id}/pay")
//    public ResponseEntity<OrderWebResponse> pay(
//            @RequestHeader("username") String username,
//            @RequestHeader("password") String password,
//            @PathVariable UUID id){
//        OrderWebResponse response = orderDetailService.pay(username, password, id);
//        if (response.getCompleted()) {
//            return ResponseEntity.ok(response);
//        } else return ResponseEntity.badRequest().build();
//
//    }
}
