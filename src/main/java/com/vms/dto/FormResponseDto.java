package com.vms.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormResponseDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isFinished")
    private boolean isFinished;

    @JsonProperty("authorizedAccounts")
    private List<AccountDto> authorizedAccounts;

    @JsonProperty("sections")
    private List<FormSectionResponseDto> formSections;
}