package com.tesobe.obp.api;

import com.tesobe.obp.domain.Bank;
import lombok.Data;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="bank", url="${obp.api.versionedUrl}")
public interface ObpBankMetaApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "banks")
    Banks getBanks();

    @Data
    class Banks {
        private List<Bank> banks;
    }
}
