package com.tesobe.obp.clientapi;

import com.tesobe.obp.domain.Token;
import lombok.val;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="account", url="${obp.api.rootUrl}")
public interface DirectAuthenticationClient {

    @PostMapping(value = "${obp.api.directLoginPath}")
    Token loginInternal(@RequestHeader("Authorization") String authHeader);

    default String login(String username, String password, String consumerKey) {
        val dlData = String.format("DirectLogin username=%s,password=%s,consumer_key=%s", username, password, consumerKey);
        val token = loginInternal(dlData).getToken();
        return token;
    }
}
