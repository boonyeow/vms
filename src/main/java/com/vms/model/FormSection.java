package com.vms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "form_section")
public class FormSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToMany
    @JoinTable(name = "formsection_account",
            joinColumns = @JoinColumn(name = "formsection_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> authorizedAccounts;

    @OneToMany(mappedBy = "form_section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Field> formFields;
}