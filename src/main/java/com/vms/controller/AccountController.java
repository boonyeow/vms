package com.vms.controller;

import com.vms.model.Account;

import com.vms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Account> getAccountByEmail(@RequestParam(value="email") String email){
        Account account = accountService.getAccountByEmail(email);
        if(account == null){
            throw new RuntimeException("account not found");
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        boolean isCreated = accountService.createAccount(account);
        if(!isCreated){
            throw new RuntimeException("account not created");
        }
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        boolean isDeleted = accountService.deleteAccount(id);
        if(!isDeleted){
            throw new RuntimeException("account not deleted");
        }
        return ResponseEntity.noContent().build();
    }
}