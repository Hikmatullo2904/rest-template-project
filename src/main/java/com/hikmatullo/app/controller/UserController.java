package com.hikmatullo.app.controller;

import com.hikmatullo.app.payload.ApiResponse;
import com.hikmatullo.app.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse saveUser(@RequestParam String login) {
        return userService.saveUser(login);
    }

}
