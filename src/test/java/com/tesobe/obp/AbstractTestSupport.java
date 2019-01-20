package com.tesobe.obp;

import com.tesobe.obp.clientapi.DirectAuthenticationClient;
import com.tesobe.obp.clientapi.ObpApiClient;
import com.tesobe.obp.clientapi.ObpBankMetaApiClient;
import com.tesobe.obp.domain.Account;
import com.tesobe.obp.domain.AccountRouting;
import com.tesobe.obp.domain.Branch;
import com.tesobe.obp.domain.User;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:auth.properties"})
@SpringBootTest
public abstract class AbstractTestSupport {
    @Value("${obp.username}")
    private String username;

    @Value("${obp.password}")
    private String password;

    @Value("${obp.consumerKey}")
    private String consumerKey;

    @Autowired private DirectAuthenticationClient authClient;
    @Autowired private ObpApiClient obpApiClient;
    @Autowired private ObpBankMetaApiClient obpBankMetaApiClient;

    @Before
    public void init() {
        String token = authClient.login(username, password, consumerKey);
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(username, token)));
        //a pre-requisite for tests is for the user to have an account
        createAccountIfNoneExists();
    }

    private void createAccountIfNoneExists() {
        User currentUser = obpApiClient.getCurrentUser();

        if(obpApiClient.getPrivateAccountsNoDetails().size() == 0) {
            //find a bank with at least a branch
            List<String> bankBranchPair = obpBankMetaApiClient.getBanks().getBanks()
                    .stream().map(bank -> {
                        {
                            try {
                                List<Branch> branches = obpBankMetaApiClient.getBranches(bank.getId()).getBranches();
                                return List.of(bank.getId(), branches.get(0).getId());
                            } catch (Exception e) {
                                //TODO: fix API not to return 400 if no branches are found for a bank
                                return Collections.<String>emptyList();
                            }
                        }
                    })
                    .filter(v -> !v.isEmpty())
                    .findFirst().get();

            String accountId = UUID.randomUUID().toString();
            Account accountRequest = new Account();
            accountRequest.setUserId(currentUser.getUserId());
            accountRequest.setBranchId(bankBranchPair.get(1));
            accountRequest.setAccountRouting(new AccountRouting("OBP", "UK123456"));
            accountRequest.setBalance(Money.zero(CurrencyUnit.EUR));
            accountRequest.setType("CURRENT");
            accountRequest.setLabel("Label1");
            Account account = obpApiClient.createAccount(bankBranchPair.get(0), accountId, accountRequest);
            Assert.assertNotNull(account);
        }
    }
}
