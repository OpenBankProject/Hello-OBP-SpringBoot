package com.tesobe.obp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.joda.money.Money;

@Data
public class Account {
    private String id;
    private String label;
    @JsonProperty("bank_id")
    private String bankId;

    @JsonDeserialize(using = MoneyJson.MoneyDeserializer.class)
    private Money balance;

    private String type;

    @JsonProperty("IBAN")
    private String iban;

    @JsonProperty("swist_bic")
    private String bic;

}
