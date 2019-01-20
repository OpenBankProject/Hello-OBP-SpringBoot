package com.tesobe.obp.api;

import com.tesobe.obp.clientapi.ObpBankMetaApiClient;
import com.tesobe.obp.domain.ATM;
import com.tesobe.obp.domain.Bank;
import com.tesobe.obp.domain.Branch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class BanksController {

    @Autowired
    private ObpBankMetaApiClient obpBankMetaApiClient;

    @GetMapping("/branches")
    public List<Branch> allBranches() {
        List<Bank> allBanks = obpBankMetaApiClient.getBanks().getBanks();
        log.info("Fetching branches for " + allBanks);
        return allBanks.stream().map(bank -> {
            try {
                List<Branch> branches = obpBankMetaApiClient.getBranches(bank.getId()).getBranches();
                return branches;
            } catch (Exception e) {
                //TODO: fix API not to return 400 if no branches are found for a bank
                return Collections.<Branch>emptyList();
            }
        }).filter(branches -> branches.size() > 0)   //exclude empty branch lists
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    @GetMapping("/atms")
    public List<ATM> allAtms() {
        List<Bank> allBanks = obpBankMetaApiClient.getBanks().getBanks();
        return allBanks.stream().map(bank -> obpBankMetaApiClient.getAtms(bank.getId()).getAtms())
                .filter(branches -> branches.size() > 0)   //exclude empty branch lists
                .flatMap(Collection::stream).collect(Collectors.toList());

    }
}
