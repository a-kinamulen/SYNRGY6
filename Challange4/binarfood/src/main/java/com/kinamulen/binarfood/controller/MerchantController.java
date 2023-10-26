package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.merchant.request.CreateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.UpdateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.response.GetMerchantWebResponse;
import com.kinamulen.binarfood.dto.merchant.response.MerchantWebResponse;
import com.kinamulen.binarfood.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public ResponseEntity<MerchantWebResponse> create(@RequestBody CreateMerchantWebRequest request){
        log.info("Starting merchant register, merchant name: {}", request.getMerchantName());
        MerchantWebResponse response = merchantService.create(request);
        return ResponseEntity.ok(response);
    }

    //Get all merchants (including closed merchants)
    @GetMapping
    public ResponseEntity<List<MerchantWebResponse>> getMerchants(@RequestHeader(value = "page", required = false, defaultValue="0") Integer page,
                                                                  @RequestHeader(value = "size", required = false, defaultValue="10") Integer size,
                                                                  @RequestHeader(value = "sortBy", required = false, defaultValue="createdAt") String sortBy,
                                                                  @RequestHeader(value = "direction", required = false, defaultValue="DESC") String direction) {
        List<MerchantWebResponse> responses = merchantService.getMerchants(page, size, sortBy, direction);
        return ResponseEntity.ok(responses);
    }

    //Get open merchants only
    @GetMapping("/open")
    public ResponseEntity<List<MerchantWebResponse>> getOpenMerchants() {
        List<MerchantWebResponse> responses = merchantService.getOpenMerchants();
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
        log.info("Starting merchant update, merchant name: {}", updateMerchantWebRequest.getMerchantName());
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
