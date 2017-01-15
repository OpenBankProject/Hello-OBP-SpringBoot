package com.tesobe.obp.transaction;

import com.tesobe.obp.AbstractTestSupport;
import com.tesobe.obp.clientapi.ObpApiClient;
import com.tesobe.obp.domain.Account;
import com.tesobe.obp.domain.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonetaryTransactionsServiceTest extends AbstractTestSupport {

    @Autowired private ObpApiClient obpApiClient;

    @Test
    public void fetchTransactionListOk() throws Exception {
        List<Account> accounts = obpApiClient.getPrivateAccountsNoDetails();
        Assert.assertTrue(accounts.size() > 0);

        List<Transaction> transactions = obpApiClient.getTransactionsForAccount(accounts.get(0).getBankId(), accounts.get(0).getId()).getTransactions();
        Assert.assertTrue(transactions.size() > 0);
    }

}