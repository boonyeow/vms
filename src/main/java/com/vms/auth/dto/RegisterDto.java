package com.vms.auth.dto;
import com.vms.model.enums.AccountType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String name;
    private String email;
    private String password;
    private String company;
    private AccountType accountType;
}
