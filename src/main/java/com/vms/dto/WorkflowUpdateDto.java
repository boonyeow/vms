package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.Form;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowUpdateDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("is_final")
    private boolean isFinal;

    @JsonProperty("workflow_form_assignments")
    private List<WorkflowFormAssignmentDto> workflowFormAssignments;
}
