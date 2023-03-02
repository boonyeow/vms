package com.vms.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "form_section")
public class FormSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "formsection_account",
            joinColumns = @JoinColumn(name = "formsection_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> authorizedUsers = new HashSet<>();

    public FormSection() {}

    public FormSection(Set<Account> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Account> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(Set<Account> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

}
