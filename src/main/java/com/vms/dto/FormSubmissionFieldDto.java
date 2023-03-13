package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormSubmissionFieldDto {
    @JsonProperty("field_id")
    private Long id;

    @JsonProperty("field_name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonProperty("help_text")
    private String helpText;

    @JsonProperty("helpText")
    @Nullable
    private List<String> options;

    @JsonProperty("next_field_id")
    @Nullable
    private Map<String, Long> nextFieldsId;

}
