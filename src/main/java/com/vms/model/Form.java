package com.vms.model;

import com.vms.model.keys.FormCompositeKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "form")
public class Form {

    @EmbeddedId
    private FormCompositeKey id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_final", nullable = false)
    private boolean isFinal;

    @ManyToMany
    @JoinTable(name = "form_account",
            joinColumns = {
                    @JoinColumn(name = "form_id", referencedColumnName = "id"),
                    @JoinColumn(name = "form_revisionNo", referencedColumnName = "revisionNo") },
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> authorizedAccounts;

    @ManyToMany(mappedBy = "forms")
    private Set<Workflow> workflows;
}
