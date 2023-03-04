package com.vms.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormSectionResponseDto {
    private Long id;
    private List<AccountDto> authorizedAccounts;
}
