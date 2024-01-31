package com.hikmatullo.app.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hikmatullo.app.entity.ExternalApiToken;
import com.hikmatullo.app.model.JwtResponse;
import com.hikmatullo.app.model.LoginRequest;
import com.hikmatullo.app.repository.ExternalApiTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthServiceImp implements AuthService {
    private final ExternalApiTokenRepository tokenRepository;
    private final RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/api";

    public AuthServiceImp(ExternalApiTokenRepository tokenRepository, RestTemplate restTemplate) {
        this.tokenRepository = tokenRepository;
        this.restTemplate = restTemplate;
    }

    public String getToken(String username) {
        Optional<ExternalApiToken> token = tokenRepository.findAllValidToken(username);
        if(token.isEmpty()) {
            return getAccessToken("404");
        }
        if( isTokenExpired(token.get().getAccessToken())) {
            return getAccessToken(token.get().getRefreshToken());
        }
        return token.get().getAccessToken();
    }

    public boolean isTokenExpired(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        String payload = splitToken[1];
        String decodedJson = new String(Base64.getUrlDecoder().decode(payload));
        JsonObject json = JsonParser.parseString(decodedJson).getAsJsonObject();

        return (json.get("exp").getAsLong() * 1000) < System.currentTimeMillis();
    }

    public String getAccessToken(String refreshToken) {
        if(refreshToken.equals("404")) {
            return "401";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(refreshToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            String refreshTokenUrl = baseUrl + "/auth/refresh";
            ResponseEntity<JwtResponse> exchange = restTemplate.exchange(refreshTokenUrl, HttpMethod.POST, entity, JwtResponse.class);
            Optional<ExternalApiToken> tokenFoundG = tokenRepository
                    .findExternalApiTokenByRefreshToken(refreshToken);

            if(tokenFoundG.isEmpty()) {
                return "401";
            }

            ExternalApiToken tokenFound = tokenFoundG.get();
            tokenFound.setId(null);
            JwtResponse body = exchange.getBody();
            if(body == null) {
                return null;
            }
            tokenFound.setRefreshToken(body.getRefreshToken());
            tokenFound.setAccessToken(body.getAccessToken());
            tokenRepository.makeTokenExpired(tokenFound.getUsername());
            tokenRepository.save(tokenFound);
            return body.getAccessToken();

        }  catch (HttpClientErrorException.NotFound ex) {
            // Handle 404 Not Found
            System.err.println("Not found");
            return "404";
        } catch (HttpClientErrorException.Unauthorized ex) {
            // Handle 401 Unauthorized
            System.err.println("Not authorized");
            return "401";

        } catch (RestClientException ex) {
            // Handle other exceptions
            System.err.println("Other exceptions");

        }
        return null;
    }

    public void getRefreshToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);
        try {
            String authenticateUrl = baseUrl + "/auth/authenticate";
            ResponseEntity<JwtResponse> exchange =
                    restTemplate.exchange(authenticateUrl, HttpMethod.POST, request, JwtResponse.class);
            tokenRepository.makeTokenExpired(username);
            ExternalApiToken token = new ExternalApiToken();
            token.setUsername(username);
            token.setAccessToken(exchange.getBody().getAccessToken());
            token.setRefreshToken(exchange.getBody().getRefreshToken());
            tokenRepository.save(token);


        }  catch (HttpClientErrorException.NotFound ex) {
            // Handle 404 Not Found
            System.err.println("Not found");
        } catch (HttpClientErrorException.Unauthorized ex) {
            // Handle 401 Unauthorized
            System.err.println("Not authorized");

        } catch (RestClientException ex) {
            // Handle other exceptions
            System.err.println("Other exceptions");

        }
    }
}
