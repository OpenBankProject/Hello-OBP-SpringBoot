package com.tesobe.obp;

import com.tesobe.obp.clientapi.DirectAuthenticationClient;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractTestSupport {
    protected String authToken;

    @Value("${obp.username}")
    private String username;

    @Value("${obp.password}")
    private String password;

    @Value("${obp.consumerKey}")
    private String consumerKey;

    @Autowired private DirectAuthenticationClient authClient;

    @Before
    public void init() {
        authClient.login(username, password, consumerKey);
    }
}
