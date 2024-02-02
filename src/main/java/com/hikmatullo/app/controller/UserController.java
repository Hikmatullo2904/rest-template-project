package com.hikmatullo.app.controller;

import com.hikmatullo.app.model.UserRequestModel;
import com.hikmatullo.app.model.UserResponseModel;
import com.hikmatullo.app.payload.ApiResponse;
import com.hikmatullo.app.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public List<UserResponseModel> getAllUsers() {
        return userService.getAllUsersWithWebClient();
    }

    @GetMapping
    public UserResponseModel getAllUsers(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping
    public ApiResponse updateUser(@RequestParam Long userId, @RequestBody UserRequestModel userRequestModel, @RequestParam String username) {
        return userService.updateUserWithWebClient(userId, userRequestModel, username);
    }

}
