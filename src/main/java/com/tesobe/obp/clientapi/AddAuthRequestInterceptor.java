package com.tesobe.obp.clientapi;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AddAuthRequestInterceptor implements RequestInterceptor {
    private String directLoginPath;
    private String username;
    private String password;
    private String consumerKey;
    DirectAuthenticationClient directAuthenticationClient;

    public AddAuthRequestInterceptor(DirectAuthenticationClient directAuthenticationClient,
                                     @Value("${obp.api.directLoginPath}") String directLoginPath,
                                     @Value("${obp.username}") String username,
                                     @Value("${obp.password}") String password,
                                     @Value("${obp.consumerKey}") String consumerKey) {

        this.directLoginPath = directLoginPath;
        this.username = username;
        this.password = password;
        this.consumerKey = consumerKey;
        this.directAuthenticationClient = directAuthenticationClient;
    }

    @Override
    public void apply(RequestTemplate template) {
        //skip login request, no auth context to add.
        if(directLoginPath.equals(template.url())) {
            return;
        }
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            String f = directAuthenticationClient.login(username, password, consumerKey);
            int t=0;
        }

        String authToken = (String) SecurityContextHolder.getContext()
                .getAuthentication().getCredentials();
        String dlHeader = String.format("DirectLogin token=%s", authToken);
        template.header(HttpHeaders.AUTHORIZATION, dlHeader);
    }
}
