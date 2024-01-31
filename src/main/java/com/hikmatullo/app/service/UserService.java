package com.hikmatullo.app.service;

import com.hikmatullo.app.payload.ApiResponse;

public interface UserService {
    ApiResponse saveUser(String username);

}
