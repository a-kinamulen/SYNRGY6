package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.merchant.request.CreateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.MerchantReportWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.UpdateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.response.GetMerchantWebResponse;
import com.kinamulen.binarfood.dto.merchant.response.MerchantWebResponse;
import com.kinamulen.binarfood.service.InvoiceService;
import com.kinamulen.binarfood.service.MerchantService;
import com.kinamulen.binarfood.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/register/_public")
    public ResponseEntity<MerchantWebResponse> create(@RequestBody CreateMerchantWebRequest request){
        log.info("Starting merchant register, merchant name: {}", request.getMerchantName());
        MerchantWebResponse response = merchantService.create(request);
        return ResponseEntity.ok(response);
    }



    //Get all merchants (including closed merchants)
    @GetMapping("/_public")
    public ResponseEntity<List<MerchantWebResponse>> getMerchants(@RequestHeader(value = "page", required = false, defaultValue="0") Integer page,
                                                                  @RequestHeader(value = "size", required = false, defaultValue="10") Integer size,
                                                                  @RequestHeader(value = "sortBy", required = false, defaultValue="createdAt") String sortBy,
                                                                  @RequestHeader(value = "direction", required = false, defaultValue="DESC") String direction) {
        List<MerchantWebResponse> responses = merchantService.getMerchants(page, size, sortBy, direction);
        return ResponseEntity.ok(responses);
    }

    //Get open merchants only
    @GetMapping("/open/_public")
    public ResponseEntity<List<MerchantWebResponse>> getOpenMerchants() {
        List<MerchantWebResponse> responses = merchantService.getOpenMerchants();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/_merchant-secured")
    public ResponseEntity<GetMerchantWebResponse> getMerchant(
            @PathVariable UUID id,
            @RequestHeader(value = "userId") String idFromToken) {
        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        GetMerchantWebResponse response = merchantService.getMerchant(id);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/_update/_merchant-secured")
    public ResponseEntity<MerchantWebResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateMerchantWebRequest updateMerchantWebRequest,
            @RequestHeader(value = "userId") String idFromToken) {
        log.info("Starting merchant update, merchant name: {}", updateMerchantWebRequest.getMerchantName());
        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        MerchantWebResponse response = merchantService.updateMerchant(id, updateMerchantWebRequest);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/_delete/_merchant-secured")
    public ResponseEntity<Boolean> delete(
            @PathVariable UUID id,
            @RequestHeader(value = "userId") String idFromToken) {
        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        boolean response = merchantService.deleteMerchant(id);
        if (response) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/report/_merchant-secured")
    public ResponseEntity<Resource> getMerchantReport(
            @PathVariable UUID id,
            @RequestBody MerchantReportWebRequest request,
            @RequestHeader(value = "userId") String idFromToken)
            throws JRException, IOException {

        if (Boolean.FALSE.equals(securityUtil.authorizeId(id, idFromToken))){
            return ResponseEntity.badRequest().build();
        }
        byte[] reportContent = invoiceService.genereteReport(id, request.getStartDate(), request.getEndDate());
        ByteArrayResource resource = new ByteArrayResource(reportContent);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("report.pdf")
                                .build().toString())
                .body(resource);
    }
}
