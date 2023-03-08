package com.vms.service;
import com.vms.dto.AccountDto;
import com.vms.dto.FormSubmissionDTO;
import com.vms.model.Account;
import com.vms.model.FormSubmission;
import com.vms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> getAccountDtoList(List<Account> authorizedAccounts){
        List<AccountDto> accountDtoList = new ArrayList<>();
        for(Account account: authorizedAccounts){
            accountDtoList.add(AccountDto.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .email(account.getEmail())
                    .accountType(account.getAccountType())
                    .build()
            );
        }
        return accountDtoList;
    }

    public Iterable<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> getAccountById(Long id){ return accountRepository.findById(id); }

    public List<FormSubmissionDTO> getFormSubmissionsByAccountID(Long id){
        List<FormSubmission> formSubmissions = accountRepository.findFormSubmissionsById(id);
        return formSubmissions.stream().map(FormSubmission::toFormSubmissionDTO).collect(Collectors.toList());
    }
}