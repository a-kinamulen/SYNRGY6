package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.merchant.request.CreateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.UpdateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.response.GetMerchantWebResponse;
import com.kinamulen.binarfood.dto.merchant.response.MerchantWebResponse;
import com.kinamulen.binarfood.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public ResponseEntity<MerchantWebResponse> create(@RequestBody CreateMerchantWebRequest request){
        MerchantWebResponse response = merchantService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MerchantWebResponse>> getMerchants() {
        List<MerchantWebResponse> responses = merchantService.getMerchants();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMerchantWebResponse> getMerchant(@PathVariable UUID id) {
        GetMerchantWebResponse response = merchantService.getMerchant(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantWebResponse> update(
            @PathVariable UUID id, @RequestBody UpdateMerchantWebRequest updateMerchantWebRequest) {
        MerchantWebResponse response = merchantService.updateMerchant(id, updateMerchantWebRequest);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        //to do: pake field isDeleted
        boolean response = merchantService.deleteMerchant(id);
        if (response) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }
}
