package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.Form;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowUpdateDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("isFinal")
    private boolean isFinal;

    @JsonProperty("form_ids")
    private List<FormRequestDto> formIds;

    @JsonProperty("authorized_accounts_ids")
    private List<Long> authorizedAccountIds;

}
