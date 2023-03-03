package com.vms.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.enums.FieldType;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonProperty("helpText")
    private String helpText;

    @JsonProperty("isRequired")
    private boolean isRequired;

    @JsonProperty("fieldType")
    private FieldType fieldType;

    @JsonProperty("options")
    private List<String> options;

    @JsonProperty("nextFields")
    private Map<String, FieldDto> nextFields;
}