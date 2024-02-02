package com.hikmatullo.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserResponseModel {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private RoleName role;
}
