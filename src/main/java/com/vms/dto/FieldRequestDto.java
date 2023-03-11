package com.vms.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.Field;
import com.vms.model.enums.FieldType;
import com.vms.model.keys.FormCompositeKey;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldRequestDto {
    private String name;
    private String label;
    private String helpText;
    private Boolean isRequired;
    private FieldType fieldType;

    @Nullable
    private Long regexId;
    @Nullable
    private List<String> options;
}