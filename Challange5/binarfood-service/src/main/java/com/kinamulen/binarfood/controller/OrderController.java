package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.service.OrderService;
import com.kinamulen.binarfood.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderWebResponse> create(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody OrderWebRequest request){
        log.info("Starting create order for user {}", username);
        OrderWebResponse response = orderService.create(username, password, request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    //getOrder
    @GetMapping("/{id}")
    public ResponseEntity<OrderWebResponse> getOrder(@PathVariable UUID id) {
        OrderWebResponse response = orderService.getOrder(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderWebResponse> pay(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @PathVariable UUID id){
        log.info("Starting pay order for user {}, with order id {}", username, id);
        OrderWebResponse response = orderService.pay(username, password, id);
        if (response.getCompleted()) {
            return ResponseEntity.ok(response);
        } else return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/paystoreprocedure")
    public ResponseEntity<OrderWebResponse> payStoreProcedure(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @PathVariable UUID id){
        log.info("Starting pay order for user {}, with order id {}", username, id);
        OrderWebResponse response = orderService.payStoreProcedure(username, password, id);
        return ResponseEntity.ok(response);
    }
}
