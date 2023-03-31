package com.vms.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.keys.FormCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowFormDto {
    @JsonProperty("id")
    private FormCompositeKey formId;

    @JsonProperty("account")
    private AccountDto account;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
