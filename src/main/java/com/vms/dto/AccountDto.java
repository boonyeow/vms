package com.vms.dto;

import com.vms.model.enums.AccountType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String name;
    private String email;
    private AccountType accountType;
}