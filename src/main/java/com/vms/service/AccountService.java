package com.vms.service;

import com.vms.dto.AccountDto;
import com.vms.model.Account;
import com.vms.model.enums.AccountType;
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

    public List<AccountDto> getAccountsByType(AccountType type){
        Iterable<Account> accounts = accountRepository.findByAccountType(type);
        return getAccountDtoList(accounts);
    }

    public List<AccountDto> getAccountDtoList(List<Account> authorizedAccounts){
        List<AccountDto> accountDtoList = new ArrayList<>();
        for(Account account: authorizedAccounts){
            accountDtoList.add(AccountDto.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .email(account.getEmail())
                    .company(account.getCompany())
                    .accountType(account.getAccountType())
                    .build()
            );
        }
        return accountDtoList;
    }

    public List<Long> getAccountIds(Iterable<Account> authorizedAccounts){
        List<Long> accountIds = new ArrayList<>();
        for(Account account: authorizedAccounts){
            accountIds.add(account.getId());
        }
        return accountIds;
    }

    public List<AccountDto> getAccountDtoList(Iterable<Account> accounts) {
        List<AccountDto> accountDtoList = new ArrayList<>();
        accounts.forEach(account -> {
            accountDtoList.add(AccountDto.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .email(account.getEmail())
                    .company(account.getCompany())
                    .accountType(account.getAccountType())
                    .build()
            );
        });
        return accountDtoList;
    }

    public List<AccountDto> getAllAccountDtoList(){
        return getAccountDtoList(accountRepository.findAll());
    }

    public void updateAccount(AccountDto request){
        Account account = getAccountById(request.getId());
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setCompany(request.getCompany());
        accountRepository.save(account);
    }
}
