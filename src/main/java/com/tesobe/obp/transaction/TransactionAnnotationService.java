package com.tesobe.obp.transaction;

import com.tesobe.obp.account.Account;
import com.tesobe.obp.account.AccountService;
import com.tesobe.obp.account.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static com.tesobe.obp.account.Transaction.Tag;

/**
 * Service that annotates transactions with tags, images and comments.
 */
@Component
public class TransactionAnnotationService {

    @Value("${obp.api.versionedUrl}")
    private String apiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AccountService accountService;

    public Tag addTag(String token, Transaction transaction, final String tagValue) {
        assert tagValue != null && tagValue.trim().length() > 0;

        //check if a tag already exists. If so, return that one.
        List<Tag> existingTags = transaction.getMetadata().getTags();
        if(existingTags != null) {
            List<Tag> matchTagz = existingTags.stream().filter(tag -> tag.getValue().equals(tagValue.trim())).collect(Collectors.toList());
            if(matchTagz.size() > 0) return matchTagz.get(0);

        }
        Account ownAccount = transaction.getOwnAccount();
        //we need the bankId
        String bankId = getBankId(token, ownAccount);

        String taggingUrl = String.format("%s/banks/%s/accounts/%s/owner/transactions/%s/metadata/tags", apiUrl, bankId, ownAccount.getId(), transaction.getId());

        HttpEntity<Tag> req = prepareAuthRequest(token, tagValue.trim());
        return restTemplate.exchange(taggingUrl, HttpMethod.POST, req, Tag.class).getBody();
    }

    public void deleteTag(String token, Transaction transaction, Tag tag) {
        Account ownAccount = transaction.getOwnAccount();
        String bankId = getBankId(token, ownAccount);

        String taggingUrl = String.format("%s/banks/%s/accounts/%s/owner/transactions/%s/metadata/tags/%s", apiUrl, bankId, ownAccount.getId(), transaction.getId(), tag.getId());
        HttpHeaders authHeaders = new HttpHeaders();
        String dlHeader = String.format("DirectLogin token=%s", token);
        authHeaders.add(HttpHeaders.AUTHORIZATION, dlHeader);
        restTemplate.exchange(taggingUrl, HttpMethod.DELETE, new HttpEntity<Void>(null, authHeaders), String.class);
    }

    private HttpEntity<Tag> prepareAuthRequest(String token, String tagValue) {
        HttpHeaders authHeaders = new HttpHeaders();
        String dlHeader = String.format("DirectLogin token=%s", token);
        authHeaders.add(HttpHeaders.AUTHORIZATION, dlHeader);
        return new HttpEntity<>(new Tag(tagValue), authHeaders);
    }

    private String getBankId(String token, Account ownAccount) {
        return accountService.fetchPrivateAccounts(token, true)
                .stream()
                .collect(Collectors.groupingBy(Account::getId))
                .get(ownAccount.getId()).get(0).getBankId();
    }
}
