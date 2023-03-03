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
public class FormDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isFinished")
    private boolean isFinished;

    @JsonProperty("authorizedAccountIds")
    private List<Long> authorizedAccountIds;

    @JsonProperty("sectionIds")
    private List<FormSectionDto> formSections;
}