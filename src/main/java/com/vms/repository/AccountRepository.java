package com.vms.repository;
import com.vms.model.Account;
//import com.vms.model.FormSubmission;
import com.vms.model.enums.AccountType;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.List;


public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
//    List<FormSubmission>findFormSubmissionsById(Long id);
    Iterable<Account> findByAccountType(AccountType accountType);
}