package com.tesobe.obp.bankmeta;

import com.tesobe.obp.AbstractTestSupport;
import com.tesobe.obp.api.ObpBankMetaApiClient;
import com.tesobe.obp.domain.Bank;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BankMetadataTest extends AbstractTestSupport {

    @Autowired private ObpBankMetaApiClient obpBankMetaApiClient;

    @Test
    public void allBanksOk() {
        List<Bank> allBanks = obpBankMetaApiClient.getBanks().getBanks();
        Assert.assertTrue(allBanks.size() > 0);
    }
}
