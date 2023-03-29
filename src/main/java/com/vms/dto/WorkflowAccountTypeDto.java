package com.vms.dto;

import com.vms.model.keys.FormCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowAccountTypeDto {
    private Long id;

    private String name;

    private int progress;

    private boolean isFinal;

    private List<WorkflowFormDto> forms;

    private List<AccountDto> authorizedAccounts;

    private Map<Long, List<FormDetailsDto>> formsAssignedToRequestedAccountType;
}
