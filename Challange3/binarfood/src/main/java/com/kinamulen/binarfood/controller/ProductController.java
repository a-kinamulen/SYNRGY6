package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.product.request.ProductWebRequest;
import com.kinamulen.binarfood.dto.product.response.ProductWebResponse;
import com.kinamulen.binarfood.service.MerchantService;
import com.kinamulen.binarfood.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public ResponseEntity<ProductWebResponse> create(@RequestHeader("merchant_name") String merchantName,
                                                     @RequestBody ProductWebRequest request){
        ProductWebResponse response = productService.create(merchantName, request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<ProductWebResponse>> getProductByMerchant(@PathVariable UUID merchantId) {
        List<ProductWebResponse> responses = productService.getProductByMerchant(merchantId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductWebResponse> getProduct(@PathVariable UUID id){
        ProductWebResponse response = productService.getProduct(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductWebResponse> update(@PathVariable UUID id, @RequestBody ProductWebRequest request){
        ProductWebResponse response = productService.update(id, request);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id){
        boolean response = productService.delete(id);
        if (response) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }
}
