package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.adapter.NotificationServiceAdapter;
import com.kinamulen.binarfood.adapter.SecurityServiceAdapter;
import com.kinamulen.binarfood.adapter.request.CreateUserCredentialWebRequest;
import com.kinamulen.binarfood.adapter.response.CreateUserCredentialWebResponse;
import com.kinamulen.binarfood.dto.order.response.OrderWebResponse;
import com.kinamulen.binarfood.dto.user.request.ForgetPasswordWebRequest;
import com.kinamulen.binarfood.dto.user.request.RegisterUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.UpdateUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.VerifyUserWebRequest;
import com.kinamulen.binarfood.dto.user.response.ForgetPasswordWebResponse;
import com.kinamulen.binarfood.dto.user.response.GetUserWebResponse;
import com.kinamulen.binarfood.dto.user.response.UserWebResponse;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.entity.*;
import com.kinamulen.binarfood.enums.UserType;
import com.kinamulen.binarfood.repository.UserDetailRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import com.kinamulen.binarfood.stream.MessageProducer;
import com.kinamulen.binarfood.stream.dto.NotificationOtpMessage;
import com.kinamulen.binarfood.stream.dto.SecurityUpdatePasswordMessage;
import com.kinamulen.binarfood.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private SecurityServiceAdapter securityServiceAdapter;
    @Autowired
    private NotificationServiceAdapter notificationServiceAdapter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private MessageProducer messageProducer;

    public UserWebResponse register(RegisterUserWebRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerified(false)
                .otp(securityUtil.generateOtp())
                .build();

        Wallet wallet = Wallet.builder()
                .balance(0.0)
                .type(UserType.USER)
                .build();

        UserDetail userDetail = UserDetail.builder()
                .phoneNumber(request.getPhoneNumber())
                .emailAddress(request.getEmailAddress())
                .isVerified(false)
                .user(user)
                .wallet(wallet)
                .build();
        user.setUserDetail(userDetail);
        wallet.setUserDetail(userDetail);
        userDetail = userDetailRepository.save(userDetail);

        //REST call to security service
        CreateUserCredentialWebResponse response = securityServiceAdapter.addNewUser(CreateUserCredentialWebRequest.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .binarfoodId(user.getId())
                        .type(UserType.USER)
                .build());

        //REST call to notification service
//        NotificationWebResponse responseNotification = notificationServiceAdapter.sendOtp(NotificationOtpWebRequest.builder()
//                        .receiverEmail(userDetail.getEmailAddress())
//                        .otp(user.getOtp())
//                        .build());

        //KAFKA PUBLISH to notification service
        messageProducer.sendMessageOtp(NotificationOtpMessage.builder()
                        .receiverEmail(userDetail.getEmailAddress())
                        .otp(user.getOtp())
                        .build());

        log.info("User CREATED: id {}, userDetailId {}, walletId {}, token {}"
                , user.getId(), userDetail.getId(), wallet.getId(), response.getToken());
        return toWebResponse(response.getToken(),user,userDetail);
    }

    public UserWebResponse forgetPassword(ForgetPasswordWebRequest request) {
        Optional<User> user = userRepository.findById(request.getId());
        if (user.isPresent()) {
            if (request.getOtp().equals(user.get().getOtp())) {
                user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
                user.get().setOtp(null);
                User updatedUser = userRepository.save(user.get());

                //REST Call - Save ke service security
//                UpdatePasswordWebResponse response = securityServiceAdapter.updatePassword(SecurityUpdatePasswordWebRequest.builder()
//                        .binarfoodId(request.getId())
//                        .newPassword(user.get().getPassword())
//                        .build());
//                log.info(response.getResponse());

                ////KAFKA PUBLISH to security service
                messageProducer.sendMessageUpdatePassword(SecurityUpdatePasswordMessage.builder()
                                .binarfoodId(request.getId())
                                .newPassword(user.get().getPassword())
                                .build());

                return toWebResponse(updatedUser, updatedUser.getUserDetail());
            }
            log.error("Verification FAILED, OTP mismatch. userId: {}", request.getId());
            return null;
        } else {
            log.error("Verification FAILED, user with id {} not found", request.getId());
            return null;
        }
    }

    public UserWebResponse verify(VerifyUserWebRequest request) {
        Optional<User> user = userRepository.findById(request.getId());
        if (user.isPresent()) {
            if (request.getOtp().equals(user.get().getOtp())) {
                user.get().setIsVerified(true);
                user.get().setOtp(null);
                User updatedUser = userRepository.save(user.get());
                log.info("User UPDATED: id {}, username {}"
                        , user.get().getId(), user.get().getUsername());
                return toWebResponse(updatedUser, updatedUser.getUserDetail());
            }
            log.error("Verification FAILED, OTP mismatch. userId: {}", request.getId());
            return null;
        } else {
            log.error("Verification FAILED, user with id {} not found", request.getId());
            return null;
        }
    }

    public ForgetPasswordWebResponse forgetPasswordTrigger(ForgetPasswordWebRequest request) {
        Optional<User> user = userRepository.findById(request.getId());
        if (user.isPresent()) {
            user.get().setOtp(securityUtil.generateOtp());
            User updatedUser = userRepository.save(user.get());
            log.info("OTP saved on database");

            //REST call to notification service
//            NotificationWebResponse responseNotification = notificationServiceAdapter.sendOtp(NotificationOtpWebRequest.builder()
//                    .receiverEmail(user.get().getUserDetail().getEmailAddress())
//                    .otp(user.get().getOtp())
//                    .build());

            //KAFKA PUBLISH to notification service
            messageProducer.sendMessageOtp(NotificationOtpMessage.builder()
                    .receiverEmail(user.get().getUserDetail().getEmailAddress())
                    .otp(user.get().getOtp())
                    .build());

            log.info("OTP sent to email");
            return ForgetPasswordWebResponse.builder()
                    .message("OTP sent")
                    .build();
        }else {
            log.error("Forget Password FAILED, user with id {} not found", request.getId());
            return null;
        }
    }



    public List<UserWebResponse> getUsers(Integer page, Integer size, String sortBy, String direction) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<User> users = userRepository.findAll(pageRequest);
        return toWebResponseList(users.toList());
    }

    public GetUserWebResponse getUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return toWebResponse(user);
    }

    public UserWebResponse updateUser(UUID id, UpdateUserWebRequest updateUserWebRequest) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setUsername(updateUserWebRequest.getUsername());
            user.get().setPassword(updateUserWebRequest.getPassword());
            user.get().getUserDetail().setEmailAddress(updateUserWebRequest.getEmailAddress());
            user.get().getUserDetail().setPhoneNumber(updateUserWebRequest.getPhoneNumber());

            User updatedUser = userRepository.save(user.get());
            log.info("User UPDATED: id {}, username {}"
                    , user.get().getId(), user.get().getUsername());
            return toWebResponse(updatedUser, updatedUser.getUserDetail());
        } else {
            log.error("Update FAILED, user with id {} not found", id);
            return null;
        }
    }

    public Boolean deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            log.info("DELETED user: username {}, id {}"
                    , user.get().getUsername(), id);
            return true;
        } else {
            log.error("Delete FAILED, user with id {} not found", id);
            return false;
        }
    }

    private GetUserWebResponse toWebResponse(Optional<User> user) {
        if (user.isPresent()) {
            return toWebResponse(user.get(), user.get().getUserDetail(), user.get().getUserDetail().getWallet(),
                    user.get().getOrders().stream().toList());
        }
        return null;
    }

    private GetUserWebResponse toWebResponse(User user, UserDetail userDetail, Wallet wallet, List<Order> orders) {
        return GetUserWebResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(userDetail.getPhoneNumber())
                .emailAddress(userDetail.getEmailAddress())
                .isDeleted(user.isDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .walletWebResponse(toWalletWebResponse(wallet))
                .orderWebResponses(toOrderWebResponseList(orders))
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

    private List<OrderWebResponse> toOrderWebResponseList(List<Order> orders) {
        List<OrderWebResponse> responses = new ArrayList<>();
        orders.forEach(order -> responses.add(
                OrderWebResponse.builder()
                        .id(order.getId())
                        .orderTime(order.getOrderTime())
                        .destinationAddress(order.getDestinationAddress())
                        .completed(order.getCompleted())
                        .totalPrice(order.getOrdersDetails().stream().mapToDouble(OrderDetail::getTotalPrice).sum())
                        .isDeleted(order.isDeleted())
                        .createdAt(order.getCreatedAt())
                        .updatedAt(order.getUpdatedAt())
                        .deletedAt(order.getDeletedAt())
                .build()));
        return responses;
    }

    private UserWebResponse toWebResponse(String token, User user, UserDetail userDetail) {
        return UserWebResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(userDetail.getPhoneNumber())
                .emailAddress(userDetail.getEmailAddress())
                .isDeleted(user.isDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    private UserWebResponse toWebResponse(User user, UserDetail userDetail) {
        return UserWebResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(userDetail.getPhoneNumber())
                .emailAddress(userDetail.getEmailAddress())
                .isDeleted(user.isDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    private List<UserWebResponse> toWebResponseList(List<User> users) {
        List<UserWebResponse> responses = new ArrayList<>();
        users.forEach(user -> responses.add(toWebResponse(user, user.getUserDetail())));
        return responses;
    }
}
