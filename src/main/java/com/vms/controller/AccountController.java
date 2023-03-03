package com.vms.controller;

import com.vms.model.Account;

import com.vms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<Iterable<Account>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/searchByEmail")
    public ResponseEntity<Optional<Account>> getAccountByEmail(@RequestParam(value="email") String email){
        Optional<Account> account = accountService.getAccountByEmail(email);
        if(account.isEmpty()){
            throw new RuntimeException("account not found");
        }
        return ResponseEntity.ok(account);
    }

}