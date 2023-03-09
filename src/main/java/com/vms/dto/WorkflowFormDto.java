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
public class WorkflowFormDto {
    @JsonProperty("formId")
    private Long formId;

    @JsonProperty("revisionId")
    private Integer revisionId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
