package com.tesobe.obp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountView {
    private String id;

    @JsonProperty("short_name")
    private String shortName;

    private String description;

    @JsonProperty("is_public")
    private Boolean isPublic;
}
