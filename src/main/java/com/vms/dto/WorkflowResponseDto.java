package com.vms.dto;

import com.vms.model.Form;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private List<AccountDto> authorizedAccounts;

    private List<Long> authorizedAccountIds;

    private List<Long> approvalSequence;
}
