package com.kinamulen.binarfood.service;


import com.kinamulen.binarfood.dto.order.request.OrderDetailWebRequest;
import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.entity.*;
import com.kinamulen.binarfood.enums.UserType;
import com.kinamulen.binarfood.repository.OrderDetailRepository;
import com.kinamulen.binarfood.repository.OrderRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    void createOrder_success() {
        //Data prep starts
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("username")
                .password("password")
                .build();
        Optional<User> optUser = Optional.of(user);

        OrderDetailWebRequest orderDetailWebRequest1 = OrderDetailWebRequest.builder()
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();
        OrderDetailWebRequest orderDetailWebRequest2 = OrderDetailWebRequest.builder()
                .productId(UUID.randomUUID())
                .quantity(2)
                .build();
        List<OrderDetailWebRequest> orderDetailWebRequests = Arrays
                .asList(orderDetailWebRequest1,orderDetailWebRequest2);
        OrderWebRequest orderWebRequest = OrderWebRequest.builder()
                .destinationAddress("home")
                .products(orderDetailWebRequests)
                .build();

        Product product = Product.builder()
                .price(10000.0)
                .build();
        Optional<Product> optProduct = Optional.of(product);


        OrderDetail orderDetail1 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(1)
                .totalPrice(100.0)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(2)
                .totalPrice(100.0)
                .build();
        Set<OrderDetail> orderDetails = Set.of(orderDetail1,orderDetail2);

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .destinationAddress("home")
                .orderTime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .ordersDetails(orderDetails)
                .build();
        //Data prep ends

        //mock data starts
        given(userRepository.findById(any(UUID.class))).willReturn(optUser);
        given(productRepository.findById(any(UUID.class))).willReturn(optProduct);
        given(orderRepository.save(any(Order.class))).willReturn(order);
        //mock data ends

        OrderWebResponse orderWebResponse = orderService
                .create(orderWebRequest, userId.toString());
        Assertions.assertNotNull(orderWebResponse);
    }


    @Test
    void createOrder_failed() {
        //mock data starts
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        //mock data ends

        OrderWebResponse orderWebResponse = orderService
                .create(OrderWebRequest.builder().build(), UUID.randomUUID().toString());
        Assertions.assertNull(orderWebResponse);
    }

    @Test
    void payOrder_success() {
        //Data prep starts
        UUID userId = UUID.randomUUID();
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(10000000000.0)
                .type(UserType.USER)
                .build();
        UserDetail userDetail = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .wallet(wallet)
                .build();
        User user = User.builder()
                .id(userId)
                .username("username")
                .password("password")
                .userDetail(userDetail)
                .build();
        Optional<User> optUser = Optional.of(user);

        OrderDetail orderDetail1 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(1)
                .totalPrice(100.0)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(2)
                .totalPrice(100.0)
                .build();
        Set<OrderDetail> orderDetails = Set.of(orderDetail1,orderDetail2);

        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .destinationAddress("home")
                .orderTime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .ordersDetails(orderDetails)
                .build();
        Optional<Order> optOrder = Optional.of(order);
        //Data prep ends

        //mock data starts
        given(userRepository.findById(any(UUID.class))).willReturn(optUser);
        given(orderRepository.findById(any(UUID.class))).willReturn(optOrder);
        //mock data ends

        OrderWebResponse orderWebResponse = orderService
                .pay(orderId, userId.toString());
        Assertions.assertNotNull(orderWebResponse);
        Assertions.assertTrue(orderWebResponse.getCompleted());
    }

    @Test
    void payOrder_insufficient_wallet_success() {
        //Data prep starts
        UUID userId = UUID.randomUUID();
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(10.0)
                .type(UserType.USER)
                .build();
        UserDetail userDetail = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .wallet(wallet)
                .build();
        User user = User.builder()
                .id(userId)
                .username("username")
                .password("password")
                .userDetail(userDetail)
                .build();
        Optional<User> optUser = Optional.of(user);

        OrderDetail orderDetail1 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(1)
                .totalPrice(100.0)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(2)
                .totalPrice(100.0)
                .build();
        Set<OrderDetail> orderDetails = Set.of(orderDetail1,orderDetail2);

        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .destinationAddress("home")
                .orderTime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .ordersDetails(orderDetails)
                .build();
        Optional<Order> optOrder = Optional.of(order);
        //Data prep ends

        //mock data starts
        given(userRepository.findById(any(UUID.class))).willReturn(optUser);
        given(orderRepository.findById(any(UUID.class))).willReturn(optOrder);
        //mock data ends

        OrderWebResponse orderWebResponse = orderService
                .pay(orderId, userId.toString());
        Assertions.assertNotNull(orderWebResponse);
        Assertions.assertFalse(orderWebResponse.getCompleted());
    }

    @Test
    void payOrder_userNotFound_fail() {
        //Data prep starts
        OrderDetail orderDetail1 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(1)
                .totalPrice(100.0)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .id(UUID.randomUUID())
                .quantity(2)
                .totalPrice(100.0)
                .build();
        Set<OrderDetail> orderDetails = Set.of(orderDetail1,orderDetail2);

        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .destinationAddress("home")
                .orderTime(LocalDateTime.now())
                .completed(false)
                .ordersDetails(orderDetails)
                .build();
        Optional<Order> optOrder = Optional.of(order);
        //Data prep ends

        //mock data starts
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        given(orderRepository.findById(any(UUID.class))).willReturn(optOrder);
        //mock data ends

        OrderWebResponse orderWebResponse = orderService
                .pay(orderId, UUID.randomUUID().toString());
        Assertions.assertNull(orderWebResponse);
    }
}