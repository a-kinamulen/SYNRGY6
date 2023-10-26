package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.merchant.request.CreateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.request.UpdateMerchantWebRequest;
import com.kinamulen.binarfood.dto.merchant.response.GetMerchantWebResponse;
import com.kinamulen.binarfood.dto.merchant.response.MerchantWebResponse;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.MerchantDetail;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.enums.WalletType;
import com.kinamulen.binarfood.repository.MerchantDetailRepository;
import com.kinamulen.binarfood.repository.MerchantRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private MerchantDetailRepository merchantDetailRepository;
    @Autowired
    private WalletRepository walletRepository;

    public MerchantWebResponse create(CreateMerchantWebRequest request) {
        Merchant merchant = Merchant.builder()
                .merchantName(request.getMerchantName())
                .open(true)
                .build();

        Wallet wallet = Wallet.builder()
                .balance(0.0)
                .type(WalletType.MERCHANT)
                .build();

        MerchantDetail merchantDetail = MerchantDetail.builder()
                .phoneNumber(request.getPhoneNumber())
                .merchantLocation(request.getMerchantLocation())
                .merchantType(request.getType())
                .merchant(merchant)
                .wallet(wallet)
                .build();

        merchant.setMerchantDetail(merchantDetail);
        wallet.setMerchantDetail(merchantDetail);
        merchantDetail = merchantDetailRepository.save(merchantDetail);
        log.info("Merchant CREATED: id {}, merchantDetailId {}, walletId {}"
                , merchant.getId(), merchantDetail.getId(), wallet.getId());
        return toWebResponse(merchant, merchantDetail);
    }

    public List<MerchantWebResponse> getMerchants(Integer page, Integer size, String sortBy, String direction) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<Merchant> merchants = merchantRepository.findAll(pageRequest);
        return toWebResponse(merchants.toList());
    }

    public List<MerchantWebResponse> getOpenMerchants() {
        List<Merchant> merchants = merchantRepository.findByOpenTrue();
        return toWebResponse(merchants);
    }

    public GetMerchantWebResponse getMerchant(UUID id) {
        Optional<Merchant> merchant = merchantRepository.findById(id);
        return toWebResponse(merchant);
    }

    public MerchantWebResponse updateMerchant(UUID id, UpdateMerchantWebRequest updateMerchantWebRequest) {
        Optional<Merchant> merchant = merchantRepository.findById(id);
        if (merchant.isPresent()) {
            merchant.get().setMerchantName(updateMerchantWebRequest.getMerchantName());
            merchant.get().setOpen(updateMerchantWebRequest.getOpen());
            merchant.get().getMerchantDetail().setPhoneNumber(updateMerchantWebRequest.getPhoneNumber());
            merchant.get().getMerchantDetail().setMerchantLocation(updateMerchantWebRequest.getMerchantLocation());
            merchant.get().getMerchantDetail().setMerchantType(updateMerchantWebRequest.getType());

            Merchant updatedMerchant = merchantRepository.save(merchant.get());
            log.info("Merchant UPDATED: id {}, merchantName {}"
                    , merchant.get().getId(), merchant.get().getMerchantName());
            return toWebResponse(updatedMerchant, updatedMerchant.getMerchantDetail());
        } else {
            log.info("Update FAILED, merchant with id {} not found", id);
            return null;
        }
    }

    public Boolean deleteMerchant(UUID id) {
        Optional<Merchant> merchant = merchantRepository.findById(id);
        if (merchant.isPresent()) {
            merchantRepository.delete(merchant.get());
            log.info("DELETED merchant {}, id {}"
                    , merchant.get().getMerchantName(), id);
            return true;
        } else {
            log.info("Delete FAILED, merchant with id {} not found", id);
            return false;
        }
    }

    private List<MerchantWebResponse> toWebResponse(List<Merchant> merchants) {
        List<MerchantWebResponse> responses = new ArrayList<>();
        merchants.forEach(merchant -> {
            responses.add(toWebResponse(merchant, merchant.getMerchantDetail()));
        });
        return responses;
    }

    private MerchantWebResponse toWebResponse(Merchant merchant, MerchantDetail merchantDetail) {
        return MerchantWebResponse.builder()
                .id(merchant.getId())
                .merchantName(merchant.getMerchantName())
                .open(merchant.getOpen())
                .phoneNumber(merchantDetail.getPhoneNumber())
                .merchantLocation(merchantDetail.getMerchantLocation())
                .type(merchantDetail.getMerchantType())
                .isDeleted(merchant.isDeleted())
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .deletedAt(merchant.getDeletedAt())
                .build();
    }

    private GetMerchantWebResponse toWebResponse(Optional<Merchant> merchant) {
        if (merchant.isPresent()) {
            return toWebResponse(merchant.get(), merchant.get().getMerchantDetail(), merchant.get().getMerchantDetail().getWallet());
        }
        return null;
    }

    private GetMerchantWebResponse toWebResponse(Merchant merchant, MerchantDetail merchantDetail, Wallet wallet) {
        return GetMerchantWebResponse.builder()
                .id(merchant.getId())
                .merchantName(merchant.getMerchantName())
                .open(merchant.getOpen())
                .phoneNumber(merchantDetail.getPhoneNumber())
                .merchantLocation(merchantDetail.getMerchantLocation())
                .type(merchantDetail.getMerchantType())
                .isDeleted(merchant.isDeleted())
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .deletedAt(merchant.getDeletedAt())
                .walletWebResponse(toWalletWebResponse(wallet))
                .build();
    }

    private WalletWebResponse toWalletWebResponse(Wallet wallet) {
        return WalletWebResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .type(wallet.getType())
                .isDeleted(wallet.isDeleted())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .deletedAt(wallet.getDeletedAt())
                .build();
    }

}
