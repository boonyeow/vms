package com.vms.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
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

    @JsonProperty("isFinal")
    private boolean isFinal;

    @JsonProperty("authorizedAccountIds")
    private List<Long> authorizedAccountIds;

    @JsonProperty("fields")
    private List<FieldRequestDto> fields;

    @JsonProperty("workflows")
    @Nullable
    private List<Long> workflows;
}