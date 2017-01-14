package com.tesobe.obp;

import com.tesobe.obp.account.Account;
import com.tesobe.obp.account.Transaction;
import com.tesobe.obp.account.Transactions;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@FeignClient(name="account", url="${obp.api.versionedUrl}")
public interface ObpApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "my/accounts")
    List<Account> getPrivateAccountsNoDetails();

    default List<Account> getPrivateAccountsWithDetails() {
        List<Account> accountsNoDetails = getPrivateAccountsNoDetails();
        return accountsNoDetails.stream().map(account -> getAccount(account.getBankId(), account.getId())).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "my/banks/{bankId}/accounts/{accountId}/account")
    Account getAccount(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId);

    @RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/owner/transactions")
    Transactions getTransactionsForAccount(@PathVariable("bankId") String bankId,
                                           @PathVariable("accountId") String accountId);

    @RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/transaction")
    Transaction getTransactionById(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                                   @PathVariable("transactionId") String transactionId);

    @RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/metadata/tags")
    Transaction.Tag addTag(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                           @PathVariable("transactionId") String transactionId, @RequestBody Transaction.Tag tag);

    @RequestMapping(method = RequestMethod.DELETE, value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/metadata/tags/{tagId}")
    void deleteTag(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                   @PathVariable("transactionId") String transactionId, @PathVariable("tagId") String tagId);
}
