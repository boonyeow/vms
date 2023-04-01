package com.vms.dto;

import com.vms.model.keys.FormCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormSubmissionWorkflowDto {
    private Long id;

    private String name;

    private int progress;

    private boolean isFinal;

    private List<FormCompositeKey> approvalSequence;
}
