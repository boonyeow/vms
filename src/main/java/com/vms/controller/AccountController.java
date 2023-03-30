package com.vms.controller;


import com.vms.dto.AccountDto;
import com.vms.model.enums.AccountType;
import com.vms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccountDtoList());
    }

    @GetMapping("/{accountType}")
    public ResponseEntity<List<AccountDto>> getAccountsByType(@PathVariable AccountType accountType){
        return ResponseEntity.ok(accountService.getAccountsByType(accountType));
    }

    @PutMapping
    public ResponseEntity<Void> updateAccountEmail(@RequestBody AccountDto request){
        accountService.updateAccount(request);
        return ResponseEntity.ok().build();
    }
}
