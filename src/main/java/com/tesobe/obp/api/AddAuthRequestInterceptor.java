package com.tesobe.obp.api;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AddAuthRequestInterceptor implements RequestInterceptor {
    @Value("${obp.api.directLoginPath}")
    private String directLoginPath;

    @Override
    public void apply(RequestTemplate template) {
        //skip login request, no auth context to add.
        if(directLoginPath.equals(template.url())) {
            return;
        }

        String authToken = (String) SecurityContextHolder.getContext()
                .getAuthentication().getCredentials();
        String dlHeader = String.format("DirectLogin token=%s", authToken);
        template.header(HttpHeaders.AUTHORIZATION, dlHeader);
    }
}
