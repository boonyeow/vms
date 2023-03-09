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
    private String name;
    private String label;
    private String helpText;
    private boolean isRequired;
    private FieldType fieldType;
    private List<String> options;
    private Map<String, Long> nextFieldsId;
}