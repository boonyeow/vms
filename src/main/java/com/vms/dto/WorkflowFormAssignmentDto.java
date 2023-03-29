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
public class WorkflowFormAssignmentDto {
    @JsonProperty("accountId")
    private Long accountId;
    @JsonProperty("formId")
    private FormRequestDto formId;
}
