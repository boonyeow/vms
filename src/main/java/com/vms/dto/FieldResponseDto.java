package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vms.model.enums.FieldType;
import com.vms.model.keys.FormCompositeKey;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldResponseDto {
    private Long id;
    private String name;
    private String helpText;
    private Boolean isRequired;
    private FieldType fieldType;
    @Nullable
    private Long regexId;
    @Nullable
    private Map<String, Long> options;
    @Nullable
    private FormCompositeKey formCompositeKey;
}
