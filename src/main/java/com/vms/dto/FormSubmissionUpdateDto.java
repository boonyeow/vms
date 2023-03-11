package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vms.model.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionUpdateDto {
    @JsonProperty("status")
    private StatusType status;

}
