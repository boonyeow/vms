package com.vms.dto;
import java.util.List;
import com.vms.model.enums.AccountType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String name;
    private String email;
    private AccountType accountType;
//    private List<FormSubmissionDTO> formsubmissions;
}