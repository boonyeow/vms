package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDetailsDto {
    @JsonProperty("form_id")
    private FormCompositeKey form_id;

    @JsonProperty("form_name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("company")
    private String company;
}
