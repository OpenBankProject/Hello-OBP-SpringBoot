package com.tesobe.obp.transaction;

import com.tesobe.obp.AbstractTestSupport;
import com.tesobe.obp.api.ObpApiClient;
import com.tesobe.obp.domain.Account;
import com.tesobe.obp.domain.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tesobe.obp.domain.Transaction.Tag;

public class TransactionAnnotationServiceTest extends AbstractTestSupport {
    @Autowired private ObpApiClient obpApiClient;

    @Test
    public void addTagOk() {
        List<Account> accounts = obpApiClient.getPrivateAccountsWithDetails();
        Account ownAccount = accounts.get(0);
        List<Transaction> transactions = obpApiClient.getTransactionsForAccount(ownAccount.getBankId(), ownAccount.getId()).getTransactions();

        Transaction tx = transactions.get(0);

        String tagValue = "food";
        Tag tag = obpApiClient.addTag(ownAccount.getBankId(), ownAccount.getId(), tx.getId(), new Tag(tagValue));
        Assert.assertNotNull(tag.getId());
        Assert.assertEquals(tagValue, tag.getValue());
        List<Tag> newTags = obpApiClient.getTransactionById(ownAccount.getBankId(), ownAccount.getId(), tx.getId()).getMetadata().getTags();
        Assert.assertTrue(newTags.contains(tag));
    }

    @Test
    public void deleteTagOk() {
        List<Account> accounts = obpApiClient.getPrivateAccountsWithDetails();
        Account ownAccount = accounts.get(0);
        List<Transaction> transactions = obpApiClient.getTransactionsForAccount(ownAccount.getBankId(), ownAccount.getId()).getTransactions();

        Transaction tx = transactions.get(0);
        //tx.getMetadata().getTags().forEach(tag -> transactionAnnotationService.deleteTag(authToken, tx, tag));
        Tag tag = obpApiClient.addTag(ownAccount.getBankId(), ownAccount.getId(), tx.getId(), new Tag("food"));
        List<Tag> txTags = obpApiClient.getTransactionById(ownAccount.getBankId(), ownAccount.getId(), tx.getId()).getMetadata().getTags();
        Assert.assertTrue(txTags.contains(tag));

        obpApiClient.deleteTag(ownAccount.getBankId(), ownAccount.getId(), tx.getId(), tag.getId());
        txTags = obpApiClient.getTransactionById(ownAccount.getBankId(), ownAccount.getId(), tx.getId()).getMetadata().getTags();
        Assert.assertTrue(!txTags.contains(tag));
    }

}