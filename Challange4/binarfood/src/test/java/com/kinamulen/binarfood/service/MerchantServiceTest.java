package com.kinamulen.binarfood.service;


import com.kinamulen.binarfood.dto.merchant.request.CreateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.UpdateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.response.GetMerchantWebResponse;
import com.kinamulen.binarfood.dto.merchant.response.MerchantWebResponse;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.MerchantDetail;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.enums.MerchantType;
import com.kinamulen.binarfood.enums.WalletType;
import com.kinamulen.binarfood.repository.MerchantDetailRepository;
import com.kinamulen.binarfood.repository.MerchantRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @InjectMocks
    private MerchantService merchantService;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private MerchantDetailRepository merchantDetailRepository;
    @Mock
    private WalletRepository walletRepository;

    @Test
    void createMerchant_success(){
        //data prep starts
        CreateMerchantWebRequest request = CreateMerchantWebRequest.builder()
                .merchantName("name")
                .phoneNumber("088888888")
                .merchantLocation("location")
                .type(MerchantType.REGULAR)
                .build();
        MerchantDetail merchantDetail = MerchantDetail.builder()
                .phoneNumber("08888888888")
                .merchantLocation("location")
                .merchantType(MerchantType.REGULAR)
                .build();
        //data prep ends

        //mock starts
        given(merchantDetailRepository.save(any(MerchantDetail.class))).willReturn(merchantDetail);
        //mock ends

        MerchantWebResponse response = merchantService.create(request);
        Assertions.assertNotNull(response);
    }

    @Test
    void getMerchant_success(){
        //data preparation starts
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(0.0)
                .type(WalletType.MERCHANT)
                .build();
        MerchantDetail merchantDetail = MerchantDetail.builder()
                .phoneNumber("08888888888")
                .merchantLocation("location")
                .merchantType(MerchantType.REGULAR)
                .wallet(wallet)
                .build();
        UUID merchantId = UUID.randomUUID();
        Optional<Merchant> optMerchant = Optional.of(Merchant.builder()
                .id(merchantId)
                .merchantDetail(merchantDetail)
                .build());
        //data preparation ends

        //mock data starts
        given(merchantRepository.findById(any(UUID.class))).willReturn(optMerchant);
        //mock data ends

        GetMerchantWebResponse getMerchantWebResponse = merchantService.getMerchant(merchantId);

        //Assertion starts
        Assertions.assertEquals(merchantId, getMerchantWebResponse.getId());
        //Assertion ends
    }

    @Test
    void getMerchant_fail(){
        UUID merchantId = UUID.randomUUID();

        //mock data starts
        given(merchantRepository.findById(merchantId)).willReturn(Optional.empty());
        //mock data ends

        GetMerchantWebResponse getMerchantWebResponse = merchantService.getMerchant(merchantId);

        //assertions starts
        Assertions.assertNull(getMerchantWebResponse);
        //assertions ends
    }

    @Test
    void getMerchants_success(){
        //data prep starts
        MerchantDetail merchantDetail1 = MerchantDetail.builder()
                .phoneNumber("088888888881")
                .merchantLocation("location1")
                .merchantType(MerchantType.REGULAR)
                .build();
        Merchant merchant1 = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantName("merchantName1")
                .merchantDetail(merchantDetail1)
                .build();
        MerchantDetail merchantDetail2 = MerchantDetail.builder()
                .phoneNumber("088888888882")
                .merchantLocation("location2")
                .merchantType(MerchantType.REGULAR)
                .build();
        Merchant merchant2 = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantName("merchantName2")
                .merchantDetail(merchantDetail2)
                .build();
        MerchantDetail merchantDetail3 = MerchantDetail.builder()
                .phoneNumber("088888888883")
                .merchantLocation("location3")
                .merchantType(MerchantType.REGULAR)
                .build();
        Merchant merchant3 = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantName("merchantName3")
                .merchantDetail(merchantDetail3)
                .build();
        List<Merchant> merchants = Arrays.asList(merchant1, merchant2, merchant3);
        //data prep ends

        //mock up starts
        given(merchantRepository.findAll()).willReturn(merchants);
        //mock up ends

        List<MerchantWebResponse> responses = merchantService.getMerchants(1,2,"id","ASC");
        Assertions.assertEquals(merchants.size(), responses.size());
    }

    @Test
    void updateMerchant_success(){
        //data preparation starts
        MerchantDetail merchantDetail1 = MerchantDetail.builder()
                .phoneNumber("088888888881")
                .merchantLocation("location1")
                .merchantType(MerchantType.REGULAR)
                .build();
        Merchant merchant1 = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantName("merchantName1")
                .merchantDetail(merchantDetail1)
                .build();
        UUID merchantId = UUID.randomUUID();
        MerchantDetail merchantDetailUpdate = MerchantDetail.builder()
                .phoneNumber("08888888888update")
                .merchantLocation("locationUpdate")
                .merchantType(MerchantType.REGULAR)
                .build();
        Merchant merchantUpdate = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantName("merchantNameUpdate")
                .merchantDetail(merchantDetailUpdate)
                .build();

        Optional<Merchant> optMerchant = Optional.of(merchant1);

        UpdateMerchantWebRequest updateMerchantWebRequest = UpdateMerchantWebRequest.builder()
                .merchantName("merchantNameUpdate")
                .phoneNumber("08888888888update")
                .merchantLocation("locationUpdate")
                .type(MerchantType.REGULAR)
                .build();
        //data preparation ends

        //mock data starts
        given(merchantRepository.findById(merchantId)).willReturn(optMerchant);
        given(merchantRepository.save(merchant1)).willReturn(merchantUpdate);
        //mock data ends

        MerchantWebResponse response = merchantService.updateMerchant(merchantId, updateMerchantWebRequest);
        Assertions.assertEquals(response.getMerchantName(), updateMerchantWebRequest.getMerchantName());
    }

    @Test
    void updateMerchant_fail(){
        //data preparation starts
        UpdateMerchantWebRequest updateMerchantWebRequest = UpdateMerchantWebRequest.builder()
                .merchantName("merchantNameUpdate")
                .phoneNumber("08888888888update")
                .merchantLocation("locationUpdate")
                .type(MerchantType.REGULAR)
                .build();
        UUID merchantId = UUID.randomUUID();
        //data preparation ends

        //mock data starts
        given(merchantRepository.findById(merchantId)).willReturn(Optional.empty());
        //mock data ends

        MerchantWebResponse response = merchantService.updateMerchant(merchantId, updateMerchantWebRequest);
        Assertions.assertNull(response);
    }

    @Test
    void deleteMerchant_success(){
        //data prep starts
        MerchantDetail merchantDetail1 = MerchantDetail.builder()
                .phoneNumber("088888888881")
                .merchantLocation("location1")
                .merchantType(MerchantType.REGULAR)
                .build();
        Merchant merchant1 = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantName("merchantName1")
                .merchantDetail(merchantDetail1)
                .build();
        UUID merchantId = UUID.randomUUID();
        Optional<Merchant> optMerchant = Optional.of(merchant1);
        //data prep ends

        //mock data starts
        given(merchantRepository.findById(merchantId)).willReturn(optMerchant);
        doNothing().when(merchantRepository).delete(merchant1);
        //mock data ends

        Boolean response = merchantService.deleteMerchant(merchantId);
        Assertions.assertTrue(response);
    }

    @Test
    void deleteMerchant_fail(){
        //data preparation starts
        UUID merchantId = UUID.randomUUID();
        //data preparation ends

        //mock data starts
        given(merchantRepository.findById(merchantId)).willReturn(Optional.empty());
        //mock data ends

        Boolean response = merchantService.deleteMerchant(merchantId);
        Assertions.assertFalse(response);
    }
}