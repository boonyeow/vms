package com.vms.config;
import com.vms.model.Account;
import com.vms.model.Regex;
import com.vms.model.enums.AccountType;
import com.vms.repository.AccountRepository;
import com.vms.repository.RegexRepository;
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

    @Autowired
    private RegexRepository regexRepository;
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findByEmail("admin@kmail.com");
        if(optionalAccount.isEmpty()){

            Account admin = Account.builder()
                    .name("admin")
                    .email("admin@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("QuantumLeap")
                    .accountType(AccountType.ADMIN)
                    .contactNumber("81118111")
                    .natureOfBusiness("Science, Tech, Engineering")
                    .registrationNumber("201616155N")
                    .gstRegistrationNumber("200312345A")
                    .isArchived(false)
                    .build();
            Account vendor = Account.builder()
                    .name("vendor")
                    .email("vendor@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("The Fake Company")
                    .accountType(AccountType.VENDOR)
                    .contactNumber("91184819")
                    .natureOfBusiness("Electricity")
                    .registrationNumber("20532514N")
                    .gstRegistrationNumber("200131322A")
                    .isArchived(false)
                    .build();
            Account vendor1 = Account.builder()
                    .name("vendor1")
                    .email("vendor1@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("Kouen")
                    .accountType(AccountType.VENDOR)
                    .contactNumber("94183841")
                    .natureOfBusiness("Exporters")
                    .registrationNumber("20523414N")
                    .gstRegistrationNumber("20852839A")
                    .isArchived(false)
                    .build();
            Account vendor2 = Account.builder()
                    .name("vendor2")
                    .email("vendor2@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("ESMOS")
                    .accountType(AccountType.VENDOR)
                    .contactNumber("84182841")
                    .natureOfBusiness("IT SAAS")
                    .registrationNumber("241324114N")
                    .gstRegistrationNumber("2084149A")
                    .isArchived(false)
                    .build();
            Account approver = Account.builder()
                    .name("approver")
                    .email("approver@kmail.com")
                    .password(passwordEncoder.encode("blopblopblop"))
                    .company("QuantumLeap")
                    .accountType(AccountType.APPROVER)
                    .contactNumber("94184814")
                    .natureOfBusiness("Science, Tech, Engineering")
                    .registrationNumber("201616155N")
                    .gstRegistrationNumber("200312345A")
                    .isArchived(false)
                    .build();

            accountRepository.saveAll(List.of(admin , vendor, vendor1, vendor2, approver));

            Regex emailRegex = Regex.builder()
                    .name("email")
                    .pattern("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")
                    .build();
            Regex urlRegex = Regex.builder()
                            .name("URL")
                                    .pattern("https?://(?:[-\\w.]|(?:%[\\da-fA-F]{2}))+")
                                            .build();
            Regex numberRegex = Regex.builder().name("number").pattern("\\d+").build();
            regexRepository.saveAll(List.of(emailRegex, urlRegex, numberRegex));
        }
    }

}