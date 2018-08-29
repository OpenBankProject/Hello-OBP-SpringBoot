package com.tesobe.obp.clientapi;

import com.tesobe.obp.domain.Token;
import lombok.val;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="account", url="${obp.api.rootUrl}")
public interface DirectAuthenticationClient {

    @RequestMapping(method = RequestMethod.POST, value = "${obp.api.directLoginPath}")
    Token loginInternal(@RequestHeader("Authorization") String authHeader);

    default String login(String username, String password, String consumerKey) {
        val dlData = String.format("DirectLogin username=\"%s\",password=\"%s\",consumer_key=\"%s\"", username, password, consumerKey);
        val token = loginInternal(dlData).getToken();
        val authentication = new UsernamePasswordAuthenticationToken(username, token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;
    }
}
