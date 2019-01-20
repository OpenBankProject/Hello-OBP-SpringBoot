package com.tesobe.obp.auth;

import com.tesobe.obp.clientapi.DirectAuthenticationClient;
import feign.FeignException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:auth.properties"})
public class DirectAuthenticationServiceTest {

    @Value("${obp.username}")
    private String username;

    @Value("${obp.password}")
    private String password;

    @Value("${obp.consumerKey}")
    private String consumerKey;

    @Autowired private DirectAuthenticationClient directAuthenticationClient;

    @Test
    public void loginOk() throws Exception {
        String token = directAuthenticationClient.login(username, password, consumerKey);
        Assert.assertNotNull(token);
    }

    @Test
    public void badCredentials() throws Exception {
        String username = "wrong";
        String password = "garble";
        try {
            directAuthenticationClient.login(username, password, "garble");
        } catch (Exception ex) {
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), ((FeignException)ex).status());
            return;
        }
        Assert.assertFalse("Should have gotten 401 exception", true);
    }

}