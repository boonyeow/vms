package com.vms.service;
import com.vms.model.Account;
import com.vms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public Iterable<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public boolean createAccount(Account account){

        if(getAccountByEmail(account.getEmail()).isEmpty()){
            // creating for the first time
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    public boolean deleteAccount(Long id){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()){
            accountRepository.delete(optionalAccount.get());
            return true;
        }
        return false;
    }


}
