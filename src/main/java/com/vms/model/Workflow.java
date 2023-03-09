package com.vms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workflow")
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int progress;

    @Column(nullable = false)
    private Boolean isFinal;

    // @ElementCollection
    private List<Long> approvalSequence;

    @ManyToMany
    @JoinTable(name = "workflow_account",
            joinColumns = @JoinColumn(name = "workflow_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> authorizedAccounts;

//    @ManyToMany
//    @JoinTable(name = "workflow_form",
//            joinColumns = @JoinColumn(name = "workflow_id"),
//            inverseJoinColumns = @JoinColumn(name = "form_id"))
//    private List<Form> forms;
//
//    @ElementCollection
//    private List<Long> formOrder;
}