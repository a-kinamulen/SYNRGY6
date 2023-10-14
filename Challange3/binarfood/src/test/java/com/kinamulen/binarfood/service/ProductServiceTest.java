package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.product.request.ProductWebRequest;
import com.kinamulen.binarfood.dto.product.response.ProductWebResponse;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.Product;
import com.kinamulen.binarfood.repository.MerchantRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void create_success(){
        ProductWebRequest request = ProductWebRequest.builder()
                .productName("product1")
                .price(100.0)
                .build();
        List<Merchant> merchants =  merchantRepository.findAll();
        ProductWebResponse response = productService.create(merchants.get(0).getMerchantName(), request);
        Assertions.assertNotNull(response);
    }

    @Test
    void create_fail(){
        ProductWebRequest request = ProductWebRequest.builder()
                .productName("product1")
                .price(100.0)
                .build();
        ProductWebResponse response = productService.create("random-name", request);
        Assertions.assertNull(response);
    }

    @Test
    void getProductByMerchant_success(){
        List<Product> products = productRepository.findAll();
        List<ProductWebResponse> responses = productService.getProductByMerchant(products.get(0).getMerchant().getId());
        Assertions.assertNotNull(responses);
    }

    @Test
    void getProductByMerchant_fail(){
        List<ProductWebResponse> responses = productService.getProductByMerchant(UUID.randomUUID());
        Assertions.assertNull(responses);
    }

    @Test
    void getProduct_success(){
        List<Product> products = productRepository.findAll();
        ProductWebResponse response = productService.getProduct(products.get(0).getId());
        Assertions.assertNotNull(response);
    }

    @Test
    void getProduct_fail(){
        ProductWebResponse response = productService.getProduct(UUID.randomUUID());
        Assertions.assertNull(response);
    }

    @Test
    void updateProduct_success(){
        ProductWebRequest request = ProductWebRequest.builder()
                .productName("updated")
                .price(100.0)
                .build();
        List<Product> products = productRepository.findAll();
        ProductWebResponse response = productService.update(products.get(0).getId(), request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getProductName(), response.getProductName());
    }

    @Test
    void updateProduct_fail(){
        ProductWebRequest request = ProductWebRequest.builder()
                .productName("updated")
                .price(100.0)
                .build();
        ProductWebResponse response = productService.update(UUID.randomUUID(), request);
        Assertions.assertNull(response);
    }

    @Test
    void deleteProduct_success(){
        List<Product> products = productRepository.findAll();
        Boolean response = productService.delete(products.get(0).getId());
        Assertions.assertTrue(response);
    }

    @Test
    void deleteProduct_fail(){
        Boolean response = productService.delete(UUID.randomUUID());
        Assertions.assertFalse(response);
    }

}