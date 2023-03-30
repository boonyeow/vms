package com.vms.dto;


import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowResponseDto {
    private Long id;
    private String name;
    private int progress;
    private boolean isFinal;
    private List<WorkflowFormDto> forms;

    private List<Long> approvalSequence;
}
