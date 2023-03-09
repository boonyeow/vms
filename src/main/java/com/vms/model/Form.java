package com.vms.model;

import com.vms.model.keys.FormCompositeKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "form")
public class Form {

    @EmbeddedId
    private FormCompositeKey id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_final", nullable = false)
    private boolean isFinal;

    @ManyToMany
    @JoinTable(name = "form_account",
            joinColumns = @JoinColumn(name = "form_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> authorizedAccounts;

    @ManyToMany(mappedBy = "forms")
    private List<Workflow> workflows;
}
