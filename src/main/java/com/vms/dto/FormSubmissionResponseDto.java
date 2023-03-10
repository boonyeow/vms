package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.enums.StatusType;
import com.vms.model.keys.FormCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("workflow_id")
    private Workflow workflow;

    @JsonProperty("fck")
    private Form form;

    @JsonProperty("status")
    private StatusType status;

    @JsonProperty("submittedBy")
    private Account submittedBy;
}
