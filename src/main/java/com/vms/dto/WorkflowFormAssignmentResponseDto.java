package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowFormAssignmentResponseDto {
    @JsonProperty("account")
    private AccountDto account;

    @JsonProperty("workflowId")
    private Long workflowId;

    @JsonProperty("formId")
    private FormRequestDto formId;

    @JsonProperty("formName")
    private String formName;
}
