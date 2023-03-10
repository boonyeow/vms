package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.keys.FormCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormSubmissionDto {
    @JsonProperty("workflow_id")
    private Long workflowId;

    @JsonProperty("fck")
    private FormCompositeKey fck;

    @JsonProperty("submittedBy")
    private Long accountId;


}
