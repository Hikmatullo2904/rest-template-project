package com.hikmatullo.app.service;

public interface AuthService {
    String getToken(String username);
    String getAccessToken(String refreshToken);
    void getRefreshToken(String username, String password);
}
