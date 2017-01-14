package com.tesobe.obp.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DirectAuthenticationService {

    @Value("${obp.api.directloginUrl}")
    private String directLoginUrl;

    @Value("${obp.consumerKey}")
    private String consumerKey;

    public String login(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String dlData = String.format("DirectLogin username=%s,password=%s,consumer_key=%s", username, password, consumerKey);
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.AUTHORIZATION, dlData);
        HttpEntity<Void> req = new HttpEntity<>(null, authHeaders);
        ResponseEntity<Token> response = restTemplate.exchange(directLoginUrl, HttpMethod.GET, req, Token.class);
        return response.getBody().getToken();
    }

    @Data
    private static class Token {
        private String token;
    }

}
