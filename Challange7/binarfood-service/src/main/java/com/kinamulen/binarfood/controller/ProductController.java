package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.product.request.ProductWebRequest;
import com.kinamulen.binarfood.dto.product.response.ProductWebResponse;
import com.kinamulen.binarfood.service.MerchantService;
import com.kinamulen.binarfood.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantService merchantService;

    @PostMapping("/_create/_merchant-secured")
    public ResponseEntity<ProductWebResponse> create(@RequestHeader("merchant_name") String merchantName,
                                                     @RequestBody ProductWebRequest request){
        log.info("Starting product creation, name: {}, on merchant: {}"
                , request.getProductName(), merchantName);
        ProductWebResponse response = productService.create(merchantName, request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    //Only get products from open merchant
    @GetMapping("/_public")
    public ResponseEntity<List<ProductWebResponse>> getProducts() {
        List<ProductWebResponse> responses = productService.getProducts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/merchant/{merchantId}/_public")
    public ResponseEntity<List<ProductWebResponse>> getProductByMerchant(@PathVariable UUID merchantId) {
        List<ProductWebResponse> responses = productService.getProductByMerchant(merchantId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/_public")
    public ResponseEntity<ProductWebResponse> getProduct(@PathVariable UUID id){
        ProductWebResponse response = productService.getProduct(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/_update/_merchant-secured")
    public ResponseEntity<ProductWebResponse> update(@PathVariable UUID id, @RequestBody ProductWebRequest request){
        ProductWebResponse response = productService.update(id, request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/_delete/_merchant-secured")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id){
        boolean response = productService.delete(id);
        if (response) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }
}
