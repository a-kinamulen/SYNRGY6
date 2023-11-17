package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.entity.Order;
import com.kinamulen.binarfood.service.InvoiceService;
import com.kinamulen.binarfood.service.JasperReportService;
import com.kinamulen.binarfood.service.OrderService;
import com.kinamulen.binarfood.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @Autowired
    private InvoiceService invoiceService;


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

    @PostMapping("/{orderId}/invoice")
    public ResponseEntity<Resource> getItemReport(@PathVariable UUID orderId)
            throws JRException, IOException {

        byte[] reportContent = invoiceService.generateInvoice(orderId);
        ByteArrayResource resource = new ByteArrayResource(reportContent);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("invoice.pdf")
                                .build().toString())
                .body(resource);
    }
}
