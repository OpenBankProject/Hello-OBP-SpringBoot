package com.tesobe.obp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

@NoArgsConstructor @AllArgsConstructor
@Data
public class TransactionRequest {

    private DestAccount to;

    @JsonDeserialize(using = MoneyJson.MoneyDeserializer.class)
    @JsonSerialize(using = MoneyJson.MoneySerializer.class)
    private Money value;

    private String description;

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class DestAccount {
        @JsonProperty("bank_id")
        private String bankId;

        @JsonProperty("account_id")
        private String accountId;
    }

}
