package com.kinamulen.binarfood.service;


import com.kinamulen.binarfood.dto.merchant.request.CreateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.UpdateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.response.GetMerchantWebResponse;
import com.kinamulen.binarfood.dto.merchant.response.MerchantWebResponse;
import com.kinamulen.binarfood.enums.MerchantType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class MerchantServiceTest {

    @Autowired
    private MerchantService merchantService;

    @Test
    void createMerchant_success(){
        CreateMerchantWebRequest request = CreateMerchantWebRequest.builder()
                .merchantName("name")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantWebResponse response = merchantService.create(request);
        Assertions.assertEquals(request.getMerchantName(),response.getMerchantName());
        Assertions.assertEquals(request.getPhoneNumber(),response.getPhoneNumber());
        Assertions.assertEquals(request.getMerchantLocation(),response.getMerchantLocation());
        Assertions.assertEquals(request.getType(),response.getType());
    }

    @Test
    void getMerchant_success(){
        CreateMerchantWebRequest request = CreateMerchantWebRequest.builder()
                .merchantName("name")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantWebResponse createRes = merchantService.create(request);

        GetMerchantWebResponse response = merchantService.getMerchant(createRes.getId());
        Assertions.assertEquals(createRes.getId(), response.getId());
    }

    @Test
    void getMerchant_fail(){
        GetMerchantWebResponse response = merchantService.getMerchant(UUID.randomUUID());
        Assertions.assertNull(response);
    }

    @Test
    void getMerchants_success(){
        List<MerchantWebResponse> responses = merchantService.getMerchants();
        Assertions.assertFalse(responses.isEmpty());
    }

    @Test
    void updateMerchant_success(){
        CreateMerchantWebRequest createReq = CreateMerchantWebRequest.builder()
                .merchantName("name")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantWebResponse createRes = merchantService.create(createReq);


        UpdateMerchantWebRequest request = UpdateMerchantWebRequest.builder()
                .merchantName("nameUpdated")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantWebResponse response = merchantService.updateMerchant(createRes.getId(), request);
        Assertions.assertEquals(createRes.getId(), response.getId());
        Assertions.assertEquals(request.getMerchantName(), response.getMerchantName());
    }

    @Test
    void updateMerchant_fail(){
        UpdateMerchantWebRequest request = UpdateMerchantWebRequest.builder()
                .merchantName("nameUpdated")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantWebResponse response = merchantService.updateMerchant(UUID.randomUUID(), request);
        Assertions.assertNull(response);
    }

    @Test
    void deleteMerchant_success(){
        CreateMerchantWebRequest createReq = CreateMerchantWebRequest.builder()
                .merchantName("name")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantWebResponse createRes = merchantService.create(createReq);

        Boolean response = merchantService.deleteMerchant(createRes.getId());
        Assertions.assertTrue(response);
    }

    @Test
    void deleteMerchant_fail(){
        Boolean response = merchantService.deleteMerchant(UUID.randomUUID());
        Assertions.assertFalse(response);
    }

}