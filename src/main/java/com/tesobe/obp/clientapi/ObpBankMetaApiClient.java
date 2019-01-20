package com.tesobe.obp.clientapi;

import com.tesobe.obp.domain.ATM;
import com.tesobe.obp.domain.Bank;
import com.tesobe.obp.domain.Branch;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="bank", url="${obp.api.versionedUrl}")
public interface ObpBankMetaApiClient {

    @GetMapping(value = "banks", consumes = MediaType.APPLICATION_JSON_VALUE)
    Banks getBanks();

    @RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/branches")
    Branches getBranches(@PathVariable("bankId") String bankId);

    @RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/branches/{branchId}")
    Branch getBranch(@PathVariable("bankId") String bankId, @PathVariable("branchId") String branchId);

    @RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/atms")
    ATMs getAtms(@PathVariable("bankId") String bankId);

    @RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/branches/{branchId}/atms/{atmId}")
    Branch getAtm(@PathVariable("bankId") String bankId, @PathVariable("branchId") String branchId, @PathVariable("atmId") String atmId);

    @Data
    class Banks {
        private List<Bank> banks;
    }

    @Data
    class ATMs {
        private List<ATM> atms;
    }

    @Data
    class Branches {
        private List<Branch> branches;
    }

}
