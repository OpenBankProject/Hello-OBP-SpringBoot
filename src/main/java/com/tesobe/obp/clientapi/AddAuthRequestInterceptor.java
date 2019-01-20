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

    public AddAuthRequestInterceptor(@Value("${obp.api.directLoginPath}") String directLoginPath) {

        this.directLoginPath = directLoginPath;
    }

    @Override
    public void apply(RequestTemplate template) {
        //skip login request, no auth context to add.
        if(directLoginPath.equals(template.url())) {
            return;
        }
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) return;

        String authToken = (String) SecurityContextHolder.getContext()
                .getAuthentication().getCredentials();
        String dlHeader = String.format("DirectLogin token=%s", authToken);
        template.header(HttpHeaders.AUTHORIZATION, dlHeader);
    }
}
