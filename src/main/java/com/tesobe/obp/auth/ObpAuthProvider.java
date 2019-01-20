package com.tesobe.obp.auth;

import com.tesobe.obp.clientapi.DirectAuthenticationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ObpAuthProvider  implements AuthenticationProvider {

    private DirectAuthenticationClient directAuthenticationClient;
    private String authToken;

    public ObpAuthProvider(DirectAuthenticationClient directAuthenticationClient,
                           @Value("${obp.consumerKey}") String authToken) {
        this.directAuthenticationClient = directAuthenticationClient;
        this.authToken = authToken;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.isAuthenticated()) {
            return authentication;
        }
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            String token = directAuthenticationClient.login(name, password, authToken);
            return new UsernamePasswordAuthenticationToken(name, token, Collections.singleton(new SimpleGrantedAuthority("USER")));
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
