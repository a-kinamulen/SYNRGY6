package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.product.request.ProductWebRequest;
import com.kinamulen.binarfood.dto.product.response.ProductWebResponse;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.Product;
import com.kinamulen.binarfood.repository.MerchantRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
            return toWebResponse(product);
        } else return null;
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

            return toWebResponse(productRepository.save(product.get()));
        } else return null;
    }

    public Boolean delete(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        } else return false;
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
                .build();
    }

}
