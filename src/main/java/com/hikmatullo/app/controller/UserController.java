package com.hikmatullo.app.controller;

import com.hikmatullo.app.payload.ApiResponse;
import com.hikmatullo.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse saveUser(@RequestParam String username) {
        return userService.saveUser(username);
    }

}
