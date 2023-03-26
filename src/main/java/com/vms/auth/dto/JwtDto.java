package com.vms.auth.dto;
import com.vms.model.enums.AccountType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    private Long accountId;
    private String token;
    private String email;
    private AccountType accountType;
}