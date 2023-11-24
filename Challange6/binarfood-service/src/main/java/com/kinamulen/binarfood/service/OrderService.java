package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.adapter.NotificationServiceAdapter;
import com.kinamulen.binarfood.adapter.request.NotificationWebRequest;
import com.kinamulen.binarfood.adapter.response.NotificationWebResponse;
import com.kinamulen.binarfood.dto.order.request.OrderDetailWebRequest;
import com.kinamulen.binarfood.dto.order.request.OrderWebRequest;
import com.kinamulen.binarfood.dto.order.response.OrderDetailWebResponse;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.entity.*;
import com.kinamulen.binarfood.repository.OrderDetailRepository;
import com.kinamulen.binarfood.repository.OrderRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private NotificationServiceAdapter notificationServiceAdapter;

    public OrderWebResponse create(OrderWebRequest request, String idFromToken) {
        Optional<User> user = userRepository.findById(UUID.fromString(idFromToken));
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (user.isPresent() && user.get().getIsVerified()) {
            //loop List<OrderDetailWebRequest>
            log.info("initiating order creation for username: {}, id: {}",
                    user.get().getUsername(), user.get().getId());
            Order order = Order.builder()
                    .orderTime(LocalDateTime.now())
                    .destinationAddress(request.getDestinationAddress())
                    .completed(false)
                    .user(user.get())
                    .ordersDetails(new HashSet<>(orderDetails))
                    .build();
            for (OrderDetailWebRequest odRequest : request.getProducts()) {
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
                    log.error("Product id {} not found. Order creation canceled", odRequest.getProductId());
                    return null;
                }
            }
            order.setOrdersDetails(new HashSet<>(orderDetails));
            order = orderRepository.save(order);
            log.info("Order created with id {}", order.getId());
            return toWebResponse(order);
        } else {
            log.error("User not found OR not verified for id: {}", idFromToken);
            return null;
        }
    }

    public OrderWebResponse getOrder(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return toWebResponse(order.get());
        } else return null;
    }

    public OrderWebResponse pay(UUID id, String idFromToken) {
        Optional<User> user = userRepository.findById(UUID.fromString(idFromToken));
        Optional<Order> order = orderRepository.findById(id);
        if (user.isPresent() && order.isPresent()) {
            log.info("initiating order payment for username: {}, order id: {}", user.get().getUsername(), id);
            Wallet userWallet = user.get().getUserDetail().getWallet();
            Double totalCost = order.get().getOrdersDetails().stream().mapToDouble(OrderDetail::getTotalPrice).sum();
            Boolean isComplete = userWallet.getBalance() >= totalCost;

            if (isComplete && !order.get().getCompleted()) {
                order.get().setCompleted(true);
                userWallet.setBalance(userWallet.getBalance() - totalCost);
                log.info("User wallet {} balance deducted {}", userWallet.getId(), totalCost);

                //merchant's wallet balance addition logic
                List<Product> productList = order.get().getOrdersDetails().stream()
                        .map(OrderDetail::getProduct)
                        .toList();
                List<Merchant> merchantList = productList.stream().distinct()
                        .map(Product::getMerchant)
                        .toList();
                //merchantId, wallet
                Map<UUID, Wallet> merchantWalletMap = merchantList.stream().distinct()
                                .collect(Collectors.toMap(Merchant::getId, merchant -> merchant.getMerchantDetail().getWallet()));

                merchantWalletMap.keySet().forEach(mw -> {
                    Double currentMerchantTotalPrice = order.get().getOrdersDetails().stream()
                            .filter(orderDetail -> orderDetail.getProduct().getMerchant().getId().equals(mw))
                            .mapToDouble(OrderDetail::getTotalPrice).sum();
                    Wallet currentMerchantWallet = merchantWalletMap.get(mw);
                    currentMerchantWallet.setBalance(currentMerchantWallet.getBalance() + currentMerchantTotalPrice);
                    log.info("Merchant wallet {} balance added {}", mw, currentMerchantTotalPrice);
                });

                orderRepository.save(order.get());

                //Send an email with invoice attached to user email
                byte[] reportContent = invoiceService.generateInvoice(id);
                String pdfInString = Base64.getEncoder().encodeToString(reportContent);
                NotificationWebResponse response = notificationServiceAdapter.sendEmail(NotificationWebRequest.builder()
                        .pdfByte(pdfInString)
                        .receiverEmail(order.get().getUser().getUserDetail().getEmailAddress())
                        .build());
                log.info("Success. " + response);
            }
            log.info("Order payment with id {} status completed: {}", order.get().getId(), order.get().getCompleted());
            return toWebResponse(order.get());
        } else {
            log.error("Username and password mismatched. Or order id not found. Order payment canceled");
            return null;
        }
    }

    public OrderWebResponse payStoreProcedure(String username, String password, UUID id) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        Optional<Order> order = orderRepository.findById(id);
        if (user.isPresent() && order.isPresent() && !order.get().getCompleted()) {
            log.info("initiating order payment for username: {}, order id: {}", username, id);
            orderRepository.deductUserWallet(id, username, password);
            orderRepository.topUpMerchantWallet(id);
            order = orderRepository.findById(id);
            //race condition causing database changes not yet reflected on order.getCompleted
            // but in database, order already updated to completed=true
            order.get().setCompleted(true);
            log.info("Order paid with id {}", order.get().getId());
            return toWebResponse(order.get());
        } else {
            log.error("Please input valid order or user info.");
            return null;
        }
    }

    private OrderWebResponse toWebResponse(Order order) {
        return OrderWebResponse.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .completed(order.getCompleted())
                .totalPrice(order.getOrdersDetails().stream().mapToDouble(OrderDetail::getTotalPrice).sum())
                .isDeleted(order.isDeleted())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .deletedAt(order.getDeletedAt())
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
                    .isDeleted(o.isDeleted())
                    .createdAt(o.getCreatedAt())
                    .updatedAt(o.getUpdatedAt())
                    .deletedAt(o.getDeletedAt())
                    .build();
            orderDetailWebResponses.add(orderDetailWebResponse);
        }
        return orderDetailWebResponses;
    }
}
