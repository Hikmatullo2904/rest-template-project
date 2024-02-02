package com.hikmatullo.app.service;

import com.hikmatullo.app.model.RoleName;
import com.hikmatullo.app.model.UserRequestModel;
import com.hikmatullo.app.model.UserResponseModel;
import com.hikmatullo.app.payload.ApiResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8080/api";


    public UserServiceImp(AuthService authService, RestTemplate restTemplate, WebClient webClient) {
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    public ApiResponse saveUser(String username) {
        UserRequestModel userRequestModel = createUser();
        HttpHeaders headers = new HttpHeaders();
        String token = authService.getToken(username);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequestModel> entity = new HttpEntity<>(userRequestModel, headers);
        String userSave = baseUrl + "/user";
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(userSave, HttpMethod.POST,entity, ApiResponse.class);

        ApiResponse body = response.getBody();
        return body;

    }

    @SuppressWarnings("unchecked")
    public List<UserResponseModel> getAllUsers() {
        String getAllUrl = baseUrl + "/user";
        HttpHeaders headers = new HttpHeaders();
        String token = authService.getToken("admin@gmail.com");
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> exchange = restTemplate.exchange(getAllUrl, HttpMethod.GET, entity, List.class);
        return exchange.getBody();
    }

    public UserResponseModel getUserByUsername(String username) {
        String getUserUrl = baseUrl + "/user/by-username";
        HttpHeaders headers = new HttpHeaders();
        String token = authService.getToken("admin@gmail.com");
        headers.setBearerAuth(token);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUserUrl);
        builder.queryParam("username", username);
        ParameterizedTypeReference<UserResponseModel> type = new ParameterizedTypeReference<>() {};
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<UserResponseModel> exchange = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, type
        );
        return exchange.getBody();

    }

    public List<UserResponseModel> getAllUsersWithWebClient() {
        String token = authService.getToken("admin@gmail.com");

        List<UserResponseModel> users = webClient.get()
                .uri("/user")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserResponseModel>>() {})
                .block();
        return users;
    }

    private UserRequestModel createUser() {
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setName("Anvarbek");
        userRequestModel.setSurname("fjdsfgdjs");
        userRequestModel.setEmail("anvarbek.fjdsfgdjs@bk.ru");
        userRequestModel.setPhoneNumber("+998990000000");
        userRequestModel.setPassword("123Admin$");
        userRequestModel.setRole(RoleName.ADMIN);
        return userRequestModel;
    }
    public ApiResponse updateUser(Long userId, UserRequestModel userRequestModel) {
        String url = "http://external-service/api/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(authService.getToken("admin@gmail.com"));

        HttpEntity<UserRequestModel> requestEntity = new HttpEntity<>(userRequestModel, headers);

        ResponseEntity<ApiResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ApiResponse.class);

        return responseEntity.getBody();
    }


    public ApiResponse updateUserWithWebClient(Long userId, UserRequestModel userRequestModel, String username) {
        String url = baseUrl + userId;

        return webClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authService.getToken(username))
                .bodyValue(userRequestModel)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();
    }

    public ApiResponse deleteUser(Long userId) {
        String url = "http://external-service/api/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getToken("admin@gmail.com"));

        ResponseEntity<ApiResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), ApiResponse.class);

        return responseEntity.getBody();
    }

    public Mono<ApiResponse> deleteUserWithWebClient(Long userId, String token) {
        String url = "http://external-service/api/users/" + userId;

        return webClient.delete()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(ApiResponse.class);
    }
}
