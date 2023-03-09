package com.vms.service;

import com.vms.dto.AccountDto;
import com.vms.model.Account;
import com.vms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Optional<Account> getAccountByEmail(String email){
        return accountRepository.findByEmail(email);
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

}
