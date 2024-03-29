package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.enums.StatusType;
import com.vms.model.keys.FormCompositeKey;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("workflow")
    private FormSubmissionWorkflowDto workflow;

    @JsonProperty("form")
    private FormSubmissionFormDto form;

    @JsonProperty("status")
    private StatusType status;

    @JsonProperty("submittedBy")
    private AccountDto submittedBy;

    @JsonProperty("fieldResponses")
    private Map<Long, String> fieldResponses;

    @JsonProperty("dateOfSubmission")
    private String dateOfSubmission;

    @JsonProperty("reviewedByAdmin")
    private AccountDto reviewedByAdmin;

    @JsonProperty("reviewedByApprover")
    private AccountDto reviewedByApprover;

}
