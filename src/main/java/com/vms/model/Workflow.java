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
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int progress;

    @Column(nullable = false)
    private boolean isFinal;

    @ElementCollection
    private List<Long> approvalSequence;

    @ManyToMany
    @JoinTable(name = "workflow_form", joinColumns = @JoinColumn(name = "workflow_id"), inverseJoinColumns = {
            @JoinColumn(name = "form_id", referencedColumnName = "id"),
            @JoinColumn(name = "form_revisionNo", referencedColumnName = "revisionNo") })
    private List<Form> forms;

    @OneToMany(mappedBy = "workflow")
    private List<WorkflowFormAssignment> workflowFormAssignments;
}