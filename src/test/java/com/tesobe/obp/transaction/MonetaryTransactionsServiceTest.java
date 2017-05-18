package com.tesobe.obp.transaction;

import com.tesobe.obp.AbstractTestSupport;
import com.tesobe.obp.clientapi.ObpApiClient;
import com.tesobe.obp.domain.Account;
import com.tesobe.obp.domain.TransactionRequest;
import com.tesobe.obp.domain.Transaction;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonetaryTransactionsServiceTest extends AbstractTestSupport {

    @Autowired private ObpApiClient obpApiClient;

    @Test
    public void fetchTransactionListOk() throws Exception {
        List<Account> accounts = obpApiClient.getPrivateAccountsNoDetails();
        Assert.assertTrue(accounts.size() > 0);

        String bankId = accounts.get(0).getBankId();
        String accountIdOne = accounts.get(0).getId();
        ObpApiClient.TransactionRequestTypes txTypes = obpApiClient.getTransactionTypes(bankId, accountIdOne);

        TransactionRequest transactionRequest = new TransactionRequest(
                new TransactionRequest.DestAccount(bankId, accountIdOne), Money.of(CurrencyUnit.EUR, 5), "some description");

        String result = obpApiClient.initiateTransaction(bankId, accounts.get(1).getId(), "SANDBOX_TAN", transactionRequest);

        List<Transaction> transactions = obpApiClient.getTransactionsForAccount(bankId, accountIdOne).getTransactions();
        Assert.assertTrue(transactions.size() > 0);
    }

}