package com.vms.model;

import com.vms.model.keys.FormCompositeKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "form")
    private List<Field> fields;

    @OneToMany(mappedBy = "form")
    private Set<WorkflowFormAssignment> workflowFormAssignments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Form form = (Form) o;
        return Objects.equals(id, form.id);
    }

    @Override
    public int hashCode() {
        // Different instances of object will always result in different hash codes
        // Even when its underlying fields are exactly the same, they represent two different hash codes
        // Use hash of FormCompositeKey to determine equality rather than memory address of object
        return Objects.hash(id);
    }
}
