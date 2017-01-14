package com.tesobe.obp.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AccountService {
    @Value("${obp.api.versionedUrl}/my/accounts")
    private String privateAccountUrl;

    @Value("${obp.api.versionedUrl}")
    private String apiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public List<Account> fetchPrivateAccounts(String token, boolean withDetails) {
        HttpEntity<Void> req = prepareAuthRequest(token);
        Account[] accounts = restTemplate.exchange(privateAccountUrl, HttpMethod.GET, req,  Account[].class).getBody();

        if(!withDetails) {
            return Arrays.asList(accounts);
        }

        return Stream.of(accounts).map(account -> {
            String acctDetailsUrl = String.format("%s/my/banks/%s/accounts/%s/account", apiUrl, account.getBankId(), account.getId());
            return restTemplate.exchange(acctDetailsUrl, HttpMethod.GET, req, Account.class).getBody();
        }).collect(Collectors.toList());
    }

    private HttpEntity<Void> prepareAuthRequest(String token) {
        HttpHeaders authHeaders = new HttpHeaders();
        String dlHeader = String.format("DirectLogin token=%s", token);
        authHeaders.add(HttpHeaders.AUTHORIZATION, dlHeader);
        return new HttpEntity<>(null, authHeaders);
    }
}
