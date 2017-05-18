package com.tesobe.obp.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.joda.money.Money;

@Data
public class TransactionRequestType {
    private String value;
    private Charge charge;

    @Data
    class Charge {
        private String summary;
        private ChargeValue value;
    }

    @Data
    static class ChargeValue {
        private String currency;

        @JsonDeserialize(using = MoneyJson.MoneyDeserializer.class)
        private Money value;
    }
}
