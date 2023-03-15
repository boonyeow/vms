package com.vms.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.keys.FormCompositeKey;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormResponseDto {
    @JsonProperty("id")
    private FormCompositeKey id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isFinal")
    private boolean isFinal;

    @JsonProperty("authorizedAccounts")
    private List<AccountDto> authorizedAccounts;

    @JsonProperty("fields")
    private List<FieldResponseDto> fields;

}