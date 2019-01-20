package com.tesobe.obp.bankmeta;

import com.tesobe.obp.AbstractTestSupport;
import com.tesobe.obp.clientapi.ObpBankMetaApiClient;
import com.tesobe.obp.domain.Bank;
import com.tesobe.obp.domain.Branch;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BankMetadataTest extends AbstractTestSupport {

    @Autowired private ObpBankMetaApiClient obpBankMetaApiClient;

    @Test
    public void allBanksOk() {
        List<Bank> allBanks = obpBankMetaApiClient.getBanks().getBanks();
        Assert.assertTrue(allBanks.size() > 0);
    }

    @Test
    public void allBranchesOk() {
        List<Bank> allBanks = obpBankMetaApiClient.getBanks().getBanks();
        Map<Bank, List<Branch>> banksWithBranches = allBanks.stream().map(bank -> {
                    List<Branch> branches = obpBankMetaApiClient.getBranches(bank.getId()).getBranches();
                    if(branches.size() == 0) return null;
                    bank.setBranches(branches);
                    return bank;
                })
                .filter(Objects::nonNull)   //exclude banks with no branches
                .collect(Collectors.toMap(b -> b, Bank::getBranches));

        Assert.assertTrue(banksWithBranches.keySet().stream().map(Bank::getBranches).mapToLong(Collection::size).sum() > 0);
    }
}
