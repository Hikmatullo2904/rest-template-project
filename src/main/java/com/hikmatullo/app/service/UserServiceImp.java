package com.hikmatullo.app.service;

import com.hikmatullo.app.model.RoleName;
import com.hikmatullo.app.model.UserModel;
import com.hikmatullo.app.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImp implements UserService {
    private final AuthService authService;
    private final RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/api";


    public UserServiceImp(AuthService authService, RestTemplate restTemplate) {
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    public ApiResponse saveUser(String username) {
        UserModel userModel = new UserModel();
        userModel.setName("Anvarbek");
        userModel.setSurname("fjdsfgdjs");
        userModel.setEmail("anvarbek.fjdsfgdjs@bk.ru");
        userModel.setPhoneNumber("+998990000000");
        userModel.setPassword("123Admin$");
        userModel.setRole(RoleName.ADMIN);
        HttpHeaders headers = new HttpHeaders();
        String token = authService.getToken(username);
        if (token.equals("401")) {
            authService.getRefreshToken(username, "123Admin$");
            token = authService.getToken(username);
        }
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserModel> entity = new HttpEntity<>(userModel, headers);
        String userSave = baseUrl + "/user";
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(userSave, HttpMethod.POST,entity, ApiResponse.class);

        ApiResponse body = response.getBody();
        return body;

    }

}
