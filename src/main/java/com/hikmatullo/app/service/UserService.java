package com.hikmatullo.app.service;

import com.hikmatullo.app.model.UserRequestModel;
import com.hikmatullo.app.model.UserResponseModel;
import com.hikmatullo.app.payload.ApiResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    ApiResponse saveUser(String username);
     List<UserResponseModel> getAllUsersWithWebClient();
     UserResponseModel getUserByUsername(String username);
     ApiResponse updateUser(Long userId, UserRequestModel userRequestModel);
     ApiResponse updateUserWithWebClient(Long userId, UserRequestModel userRequestModel, String username);
     ApiResponse deleteUser(Long userId);
     Mono<ApiResponse> deleteUserWithWebClient(Long userId, String token);
}
