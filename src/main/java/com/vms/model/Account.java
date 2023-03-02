package com.vms.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name="account")
public class Account {

    public enum AccountType {
        ADMIN,
        VENDOR,
        APPROVER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name="account_type", nullable = false)
    private AccountType accountType;

    @ManyToMany(mappedBy = "authorizedUsers")
    private Set<FormSection> authorizedFormSections = new HashSet<>();

    // Getters
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}