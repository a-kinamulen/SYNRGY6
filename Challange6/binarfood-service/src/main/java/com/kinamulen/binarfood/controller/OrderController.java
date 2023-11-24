package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.entity.Order;
import com.kinamulen.binarfood.service.InvoiceService;
import com.kinamulen.binarfood.service.JasperReportService;
import com.kinamulen.binarfood.service.OrderService;
import com.kinamulen.binarfood.service.UserService;
import com.kinamulen.binarfood.util.SecurityUtil;
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
    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/_user-secured")
    public ResponseEntity<OrderWebResponse> create(
            @RequestBody OrderWebRequest request,
            @RequestHeader(value = "userId") String idFromToken){
        log.info("Starting create order for user {}", idFromToken);
        OrderWebResponse response = orderService.create(request, idFromToken);
        if (Objects.nonNull(response)) {

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    //getOrderbyId
    @GetMapping(value = "/{id}/_user-secured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderWebResponse> getOrder(
            @PathVariable UUID id,
            @RequestHeader(value = "userId") String idFromToken) {
        if (Boolean.FALSE.equals(securityUtil.authorizeOrderToUserId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        OrderWebResponse response = orderService.getOrder(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/pay/_user-secured")
    public ResponseEntity<OrderWebResponse> pay(
            @PathVariable UUID id,
            @RequestHeader(value = "userId") String idFromToken){
        log.info("Starting pay order for user {}, with order id {}", idFromToken, id);
        if (Boolean.FALSE.equals(securityUtil.authorizeOrderToUserId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        OrderWebResponse response = orderService.pay(id, idFromToken);
        if (Boolean.TRUE.equals(response.getCompleted())) {
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

    @PostMapping("/{orderId}/invoice/_user-secured")
    public ResponseEntity<Resource> getItemReport(@PathVariable UUID orderId,
                                                  @RequestHeader(value = "userId") String idFromToken)
            throws JRException, IOException {

        if (Boolean.FALSE.equals(securityUtil.authorizeOrderToUserId(orderId, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
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
