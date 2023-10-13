package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderDetailWebResponse;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.entity.*;
import com.kinamulen.binarfood.repository.OrderDetailRepository;
import com.kinamulen.binarfood.repository.OrderRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public OrderWebResponse create(String username, String password, OrderWebRequest request) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (user.isPresent()) {
            //loop List<OrderDetailWebRequest>
            Order order = Order.builder()
                    .orderTime(LocalDateTime.now())
                    .destinationAddress(request.getDestinationAddress())
                    .completed(false)
                    .user(user.get())
                    .ordersDetails(new HashSet<>(orderDetails))
                    .build();
            request.getProducts().forEach(odRequest -> {
                try {
                    Optional<Product> product = productRepository.findById(odRequest.getProductId());
                    if (product.isPresent()) {
                        OrderDetail orderDetail = OrderDetail.builder()
                                .quantity(odRequest.getQuantity())
                                .totalPrice(product.get().getPrice() * odRequest.getQuantity())
                                .order(order)
                                .product(product.get())
                                .build();
                        orderDetails.add(orderDetail);
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            order.setOrdersDetails(new HashSet<>(orderDetails));
            return toWebResponse(orderRepository.save(order));
        } else return null;
    }

    private OrderWebResponse toWebResponse(Order order) {
        return OrderWebResponse.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .completed(order.getCompleted())
                .totalPrice(order.getOrdersDetails().stream().mapToDouble(OrderDetail::getTotalPrice).sum())
                .orderDetailWebResponses(toOrderDetailsWebResponse(order.getOrdersDetails()))
                .build();
    }

    private List<OrderDetailWebResponse> toOrderDetailsWebResponse(Set<OrderDetail> ordersDetails) {
        List<OrderDetailWebResponse> orderDetailWebResponses = new ArrayList<>();
        for (OrderDetail o : ordersDetails) {
            OrderDetailWebResponse orderDetailWebResponse = OrderDetailWebResponse.builder()
                    .id(o.getId())
                    .quantity(o.getQuantity())
                    .totalPrice(o.getTotalPrice())
                    .build();
            orderDetailWebResponses.add(orderDetailWebResponse);
        }
        return orderDetailWebResponses;
    }

//    public OrderWebResponse pay(String username, String password, UUID id) {
//        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
//        Optional<Order> order = orderRepository.findById(id);
//        if (user.isPresent() && order.isPresent()) {
//            Wallet userWallet = user.get().getUserDetail().getWallet();
//            Double totalCost = order.get().getOrdersDetails().stream().mapToDouble(OrderDetail::getTotalPrice).sum();
//            Boolean isComplete = userWallet.getBalance() >= totalCost;
//
//            if (isComplete) {
//                order.get().setCompleted(true);
//                userWallet.setBalance(userWallet.getBalance() - totalCost);
//                Wallet merchantWallet =
//
//            }
//        }
//    }
}
