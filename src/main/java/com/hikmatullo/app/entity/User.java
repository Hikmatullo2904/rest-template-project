package com.hikmatullo.app.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
}
