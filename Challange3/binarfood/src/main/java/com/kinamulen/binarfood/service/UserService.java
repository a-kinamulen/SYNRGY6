package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.user.request.RegisterUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.UpdateUserWebRequest;
import com.kinamulen.binarfood.dto.user.response.GetUserWebResponse;
import com.kinamulen.binarfood.dto.user.response.UserWebResponse;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.entity.User;
import com.kinamulen.binarfood.entity.UserDetail;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.enums.WalletType;
import com.kinamulen.binarfood.repository.UserDetailRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;

    public UserWebResponse register(RegisterUserWebRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        Wallet wallet = Wallet.builder()
                .balance(0.0)
                .type(WalletType.USER)
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
        return toWebResponse(user,userDetail);
    }

    public List<UserWebResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return toWebResponseList(users);
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
            return toWebResponse(updatedUser, updatedUser.getUserDetail());
        } else return null;
    }

    public Boolean deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        } else return false;
    }

    private GetUserWebResponse toWebResponse(Optional<User> user) {
        if (user.isPresent()) {
            return toWebResponse(user.get(), user.get().getUserDetail(), user.get().getUserDetail().getWallet());
        }
        return null;
    }

    private GetUserWebResponse toWebResponse(User user, UserDetail userDetail, Wallet wallet) {
        return GetUserWebResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(userDetail.getPhoneNumber())
                .emailAddress(userDetail.getEmailAddress())
                .walletWebResponse(toWalletWebResponse(wallet))
                .build();
    }

    private WalletWebResponse toWalletWebResponse(Wallet wallet) {
        return WalletWebResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .type(wallet.getType())
                .build();
    }

    private UserWebResponse toWebResponse(User user, UserDetail userDetail) {
        return UserWebResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(userDetail.getPhoneNumber())
                .emailAddress(userDetail.getEmailAddress())
                .build();
    }

    private List<UserWebResponse> toWebResponseList(List<User> users) {
        List<UserWebResponse> responses = new ArrayList<>();
        users.forEach(user -> responses.add(toWebResponse(user, user.getUserDetail())));
        return responses;
    }

}
