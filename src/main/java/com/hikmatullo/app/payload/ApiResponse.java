package com.hikmatullo.app.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class ApiResponse {
    private String message;
    private HttpStatus httpStatus;
}
