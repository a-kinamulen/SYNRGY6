package com.kinamulen.binarfood.service;


import com.kinamulen.binarfood.dto.user.request.RegisterUserWebRequest;
import com.kinamulen.binarfood.dto.user.request.UpdateUserWebRequest;
import com.kinamulen.binarfood.dto.user.response.GetUserWebResponse;
import com.kinamulen.binarfood.dto.user.response.UserWebResponse;
import com.kinamulen.binarfood.entity.User;
import com.kinamulen.binarfood.entity.UserDetail;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.enums.WalletType;
import com.kinamulen.binarfood.repository.UserDetailRepository;
import com.kinamulen.binarfood.repository.UserRepository;
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
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailRepository userDetailRepository;

    @Test
    void createUser_success(){
        //data prep starts
        RegisterUserWebRequest request = RegisterUserWebRequest.builder()
                .username("username")
                .password("password")
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .build();
        UserDetail userDetail = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .build();
        //data prep ends

        //mock starts
        given(userDetailRepository.save(any(UserDetail.class))).willReturn(userDetail);
        //mock ends

        UserWebResponse response = userService.register(request);
        Assertions.assertNotNull(response);
    }

    @Test
    void getUser_success(){
        //data preparation starts
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(0.0)
                .type(WalletType.USER)
                .build();
        UserDetail userDetail = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .wallet(wallet)
                .build();
        UUID userId = UUID.randomUUID();
        Optional<User> optUser = Optional.of(User.builder().id(userId).userDetail(userDetail).build());
        //data preparation ends

        //mock data starts
        given(userRepository.findById(any(UUID.class))).willReturn(optUser);
        //mock data ends

        GetUserWebResponse getUserWebResponse = userService.getUser(userId);

        //assertions starts
        Assertions.assertEquals(userId, getUserWebResponse.getId());
        //assertions ends
    }

    @Test
    void getUser_fail(){
        UUID userId = UUID.randomUUID();

        //mock data starts
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        //mock data ends

        GetUserWebResponse getUserWebResponse = userService.getUser(userId);

        //assertions starts
        Assertions.assertNull(getUserWebResponse);
        //assertions ends
    }

    @Test
    void getUsers_success(){
        //data preparation starts
        UserDetail userDetail1 = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .build();
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .userDetail(userDetail1)
                .build();
        UserDetail userDetail2 = UserDetail.builder()
                .phoneNumber("088888888882")
                .emailAddress("email2@mail.com")
                .build();
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("username2")
                .userDetail(userDetail2)
                .build();
        UserDetail userDetail3 = UserDetail.builder()
                .phoneNumber("088888888883")
                .emailAddress("email3@mail.com")
                .build();
        User user3 = User.builder()
                .id(UUID.randomUUID())
                .username("username3")
                .userDetail(userDetail3)
                .build();

        List<User> users = Arrays.asList(user1, user2, user3);
        //data preparation ends

        //mock starts here
        given(userRepository.findAll()).willReturn(users);
        //mock ends here

        List<UserWebResponse> responses = userService.getUsers();
        Assertions.assertEquals(users.size(), responses.size());
    }

    @Test
    void updateUser_success(){
        //data preparation starts
        UserDetail userDetail = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .build();
        UUID userId = UUID.randomUUID();
        User user = User.builder().username("username").password("password").id(userId).userDetail(userDetail).build();

        UserDetail updatedUserDetail = UserDetail.builder()
                .phoneNumber("0888833333")
                .emailAddress("emailUpdated@mail.com")
                .build();
        User updatedUser = User.builder().username("usernameUpdated").password("passwordUpdated").id(userId).userDetail(updatedUserDetail).build();
        Optional<User> optUser = Optional.of(user);

        UpdateUserWebRequest updateUserWebRequest = UpdateUserWebRequest.builder()
                .username("usernameUpdated")
                .password("passwordUpdated")
                .phoneNumber("0888833333")
                .emailAddress("emailUpdated@mail.com")
                .build();
        //data preparation ends

        //mock data starts
        given(userRepository.findById(userId)).willReturn(optUser);
        given(userRepository.save(user)).willReturn(updatedUser);
        //mock data ends

        UserWebResponse response = userService.updateUser(userId, updateUserWebRequest);
        Assertions.assertEquals(response.getUsername(), updateUserWebRequest.getUsername());
    }

    @Test
    void updateUser_fail(){
        //data preparation starts
        UpdateUserWebRequest updateUserWebRequest = UpdateUserWebRequest.builder()
                .username("usernameUpdated")
                .password("passwordUpdated")
                .phoneNumber("0888833333")
                .emailAddress("emailUpdated@mail.com")
                .build();
        UUID userId = UUID.randomUUID();
        //data preparation ends

        //mock data starts
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        //mock data ends

        UserWebResponse response = userService.updateUser(userId, updateUserWebRequest);
        Assertions.assertNull(response);
    }

    @Test
    void deleteUser_success(){
        //data preparation starts
        UUID userId = UUID.randomUUID();
        UserDetail userDetail = UserDetail.builder()
                .phoneNumber("08888888888")
                .emailAddress("email@mail.com")
                .build();
        User user = User.builder().username("username").password("password").id(userId).userDetail(userDetail).build();
        Optional<User> optUser = Optional.of(user);
        //data preparation ends

        //mock data starts
        given(userRepository.findById(userId)).willReturn(optUser);
        doNothing().when(userRepository).delete(user);
        //mock data ends

        Boolean response = userService.deleteUser(userId);
        Assertions.assertTrue(response);
    }

    @Test
    void deleteUser_fail(){
        //data preparation starts
        UUID userId = UUID.randomUUID();
        //data preparation ends

        //mock data starts
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        //mock data ends

        Boolean response = userService.deleteUser(userId);
        Assertions.assertFalse(response);
    }

}