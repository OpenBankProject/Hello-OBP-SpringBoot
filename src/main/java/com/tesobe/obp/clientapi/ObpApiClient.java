package com.tesobe.obp.clientapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tesobe.obp.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@FeignClient(name="account", url="${obp.api.versionedUrl}")
public interface ObpApiClient {

    //tag::my-account[]
    @GetMapping(value = "my/accounts")
    List<Account> getPrivateAccountsNoDetails();

    default List<Account> getPrivateAccountsWithDetails() {
        List<Account> accountsNoDetails = getPrivateAccountsNoDetails();
        return accountsNoDetails.stream().map(account -> getAccount(account.getBankId(), account.getId())).collect(Collectors.toList());
    }

    @GetMapping(value = "my/banks/{bankId}/accounts/{accountId}/account")
    Account getAccount(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId);

    @GetMapping(value = "banks/{bankId}/accounts/{accountId}/views")
    AccountViews getViewsForAccount(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId);

    @GetMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions")
    Transactions getTransactionsForAccount(@PathVariable("bankId") String bankId,
                                           @PathVariable("accountId") String accountId);

    @GetMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/transaction")
    Transaction getTransactionById(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                                   @PathVariable("transactionId") String transactionId);

    @GetMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions")
    String transferMoney(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId, @RequestBody TransactionRequest transfer);
    //end::my-account[]

    @GetMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transaction-request-types")
    TransactionRequestTypes getTransactionTypes(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId);

    @PostMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transaction-request-types/{transactionReqType}/transaction-requests")
    String initiateTransaction(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                               @PathVariable("transactionReqType") String transactionReqType, @RequestBody TransactionRequest txRequest);

    //tag::public-accounts[]
    @GetMapping(value = "accounts")
    List<Account> getAllPublicAccountsAtAllBanks();

    @PutMapping("/banks/{bankId}/accounts/{accountId}")
    Account createAccount(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId, @RequestBody Account accountRequest);

    @GetMapping("users/current")
    User getCurrentUser();

    //end::public-accounts[]

    //tag::tx-metadata[]
    @PostMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/metadata/tags")
    Transaction.Tag tagTransaction(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                                   @PathVariable("transactionId") String transactionId, @RequestBody Transaction.Tag tag);

    @DeleteMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/metadata/tags/{tagId}")
    void deleteTransactionTag(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                              @PathVariable("transactionId") String transactionId, @PathVariable("tagId") String tagId);

    @PostMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/metadata/where")
    void addLocation(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                           @PathVariable("transactionId") String transactionId, @RequestBody Where location);

    @DeleteMapping(value = "banks/{bankId}/accounts/{accountId}/owner/transactions/{transactionId}/metadata/where")
    void deleteLocation(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
                   @PathVariable("transactionId") String transactionId);
    //end::tx-metadata[]

    @Data
    class Transactions {
        private List<Transaction> transactions;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    class Where {
        @JsonProperty("where")
        private Location location;
    }

    @Data
    class TransactionRequestTypes {
        @JsonProperty("transaction_request_types")
        private List<TransactionRequestType> transactionRequests;
    }

    @Data
    class AccountViews {
        private List<AccountView> views;
    }
}
