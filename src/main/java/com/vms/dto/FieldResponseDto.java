package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vms.model.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldResponseDto {

    private String id;
    private String fieldId;
    private FieldType fieldType;
    private String responseText;
    private String formSubmissionId;

}
