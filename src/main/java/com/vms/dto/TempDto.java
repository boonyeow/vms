package com.vms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempDto {
    private Long id;
    private String name;
    private int progress;
    private boolean isFinal;
    private List<WorkflowFormV2Dto> forms;

}
