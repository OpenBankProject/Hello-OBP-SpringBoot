package com.tesobe.obp.auth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DirectAuthenticationServiceTest {

    @Value("${obp.username}")
    private String username;

    @Value("${obp.password}")
    private String password;

    @Autowired private DirectAuthenticationService directAuthenticationService;

    @Test
    public void loginOk() throws Exception {
        String token = directAuthenticationService.login(username, password);
        Assert.assertNotNull(token);
    }

    @Test(expected = HttpClientErrorException.class)
    public void badCredentials() throws Exception {
        String username = "wrong";
        String password = "garble";
        String token = directAuthenticationService.login(username, password);
        Assert.assertNotNull(token);
    }

}