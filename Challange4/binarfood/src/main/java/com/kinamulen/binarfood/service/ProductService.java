package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.dto.product.request.ProductWebRequest;
import com.kinamulen.binarfood.dto.product.response.ProductWebResponse;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.OrderDetail;
import com.kinamulen.binarfood.entity.Product;
import com.kinamulen.binarfood.repository.MerchantRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    public ProductWebResponse create(String merchantName, ProductWebRequest request) {
        Optional<Merchant> merchant = merchantRepository.findByMerchantName(merchantName);
        if (merchant.isPresent()) {
            Product product = Product.builder()
                    .productName(request.getProductName())
                    .price(request.getPrice())
                    .merchant(merchant.get())
                    .build();
            product = productRepository.save(product);
            log.info("Product {} on merchant {} CREATED with id {}"
                    , product.getProductName(), merchantName, product.getId());
            return toWebResponse(product);
        } else {
            log.info("Cant find merchant: {}, FAILED to create product", merchantName);
            return null;
        }
    }

    public List<ProductWebResponse> getProducts() {
//        List<Product> products = productRepository.findAll();
//        products.stream().filter(product -> product.getMerchant().getOpen());
        List<Product> products = productRepository.findProductByOpenMerchant();
        return toWebResponseList(products);
    }


    public List<ProductWebResponse> getProductByMerchant(UUID merchantId) {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if (merchant.isPresent()) {
            return toWebResponseList(merchant.get());
        } else return null;
    }

    public ProductWebResponse getProduct(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return toWebResponse(product.get());
        } else return null;
    }
    public ProductWebResponse update(UUID id, ProductWebRequest request) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().setProductName(request.getProductName());
            product.get().setPrice(request.getPrice());

            log.info("{} : Updated product {}, price {}, id {}"
                    , product.get().getUpdatedAt(), product.get().getProductName(), product.get().getPrice(), id);
            return toWebResponse(productRepository.save(product.get()));
        } else {
            log.error("product id {} not found, update failed", id);
            return null;
        }
    }

    public Boolean delete(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        } else return false;
    }

    private List<ProductWebResponse> toWebResponseList(List<Product> products) {
        List<ProductWebResponse> responses = new ArrayList<>();
        products.forEach(product -> responses.add(
                ProductWebResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .merchantName(product.getMerchant().getMerchantName())
                        .isDeleted(product.isDeleted())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .deletedAt(product.getDeletedAt())
                        .build()));
        return responses;
    }

    private List<ProductWebResponse> toWebResponseList(Merchant merchant) {
        List<ProductWebResponse> responses = new ArrayList<>();
        merchant.getProducts().forEach(product -> responses.add(toWebResponse(product)));
        return responses;
    }

    private ProductWebResponse toWebResponse(Product product) {
        return ProductWebResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .merchantName(product.getMerchant().getMerchantName())
                .isDeleted(product.isDeleted())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .deletedAt(product.getDeletedAt())
                .build();
    }
}
