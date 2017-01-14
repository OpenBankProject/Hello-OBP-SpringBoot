package com.tesobe.obp.transaction;

import com.tesobe.obp.account.Account;
import com.tesobe.obp.account.Transaction;
import lombok.Data;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MonetaryTransactionsService {

    @Value("${obp.api.versionedUrl}")
    private String apiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public List<Transaction> fetchTransactionList(String token, Account account) {
        String allTransactionsUrl = String.format("%s/banks/%s/accounts/%s/owner/transactions", apiUrl, account.getBankId(), account.getId());
        HttpEntity<Void> req = prepareAuthRequest(token);
        Transactions transactions = restTemplate.exchange(allTransactionsUrl, HttpMethod.GET, req, Transactions.class).getBody();
        return transactions.getTransactions();
    }

    public Transaction getTransactionById(String token, Account account, String txId) {
        String txUrl = String.format("%s/banks/%s/accounts/%s/owner/transactions/%s/transaction", apiUrl, account.getBankId(), account.getId(), txId);
        HttpEntity<Void> req = prepareAuthRequest(token);
        return restTemplate.exchange(txUrl, HttpMethod.GET, req, Transaction.class).getBody();
    }

    private HttpEntity<Void> prepareAuthRequest(String token) {
        HttpHeaders authHeaders = new HttpHeaders();
        String dlHeader = String.format("DirectLogin token=%s", token);
        authHeaders.add(HttpHeaders.AUTHORIZATION, dlHeader);
        return new HttpEntity<>(null, authHeaders);
    }

    public void transferMoney(String token, Account sourceAccount, Account targetAccount, Money amount) {

    }

    @Data
    private static class Transactions {
        private List<Transaction> transactions;
    }
}
