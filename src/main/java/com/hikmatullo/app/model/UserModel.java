package com.hikmatullo.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserModel {
    private Long id;
    private String email;
    private String phoneNumber;
    private String name;
    private String surname;
    private String password;
    private RoleName role;
}
