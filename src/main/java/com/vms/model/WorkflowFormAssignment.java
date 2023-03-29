package com.vms.model;

import com.vms.model.keys.FormCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="workflow_form_assignment")
public class WorkflowFormAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "form_id", referencedColumnName = "id"),
            @JoinColumn(name = "form_revision_no", referencedColumnName = "revisionNo")
    })
    private Form form;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
