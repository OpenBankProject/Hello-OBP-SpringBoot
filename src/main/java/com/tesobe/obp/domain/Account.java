package com.tesobe.obp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

@Data
public class Account {
    private String id;

    @JsonProperty("user_id")
    private String userId;

    private String label;

    @JsonProperty("bank_id")
    private String bankId;

    @JsonProperty("branch_id")
    private String branchId;

    @JsonDeserialize(using = MoneyJson.MoneyDeserializer.class)
    @JsonSerialize(using = MoneyJson.MoneySerializer.class)
    private Money balance;

    private String type;

    @JsonProperty("IBAN")
    private String iban;

    @JsonProperty("swist_bic")
    private String bic;

    @JsonProperty("account_routing")
    private AccountRouting accountRouting;

}
