package com.vms.config;
import com.vms.model.Account;
import com.vms.model.enums.AccountType;
import com.vms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findByEmail("admin@kmail.com");
        if(optionalAccount.isEmpty()){

            Account admin = Account.builder()
                    .name("admin")
                    .email("admin@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("HELLOOOOOOOOO")
                    .accountType(AccountType.ADMIN)
                    .build();
            Account vendor = Account.builder()
                    .name("vendor")
                    .email("vendor@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("HELLOOOOOOOOO")
                    .accountType(AccountType.VENDOR)
                    .build();
            Account approver = Account.builder()
                    .name("approver")
                    .email("approver@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("HELLOOOOOOOOO")
                    .accountType(AccountType.APPROVER)
                    .build();

            accountRepository.saveAll(List.of(admin , vendor, approver));
        }
    }

}