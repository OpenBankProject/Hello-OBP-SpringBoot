package com.tesobe.obp.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;

public class MoneyJson {

    public static class MoneyDeserializer extends JsonObjectDeserializer<Money> {
        @Override
        protected Money deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec, JsonNode tree) throws IOException {
            return Money.of(CurrencyUnit.of(tree.get("currency").asText()), Double.parseDouble(tree.get("amount").textValue()));
        }
    }

    public static class MoneySerializer extends JsonObjectSerializer<Money> {
    @Override
    protected void serializeObject(Money money, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStringField("currency", money.getCurrencyUnit().getCode());
            jgen.writeStringField("amount", String.format("%.2f", money.getAmount().floatValue()));
        }
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    static class MonetaryVal {
        private String currency;
        private Double amount;
    }

}
