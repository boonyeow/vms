package com.vms.repository;

import com.vms.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    public Account findByEmail(String email);
}