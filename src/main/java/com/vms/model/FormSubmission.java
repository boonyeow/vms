package com.vms.model;

import com.vms.model.enums.StatusType;
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
@Table(name = "FormSubmission")
public class FormSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    // This Form field does not update even if you update the corresponding Form instance.
    // Just for safety purpose but we still must not allow editing to the Form directly without creating a new Form instance
    // Otherwise, some functions will not work like the GetFormSubmissionsByWorkflowAndForm
    // For deleting, if you delete the corresponding Form instance, it SHOULD still exist here but should still refrain from deleting Form
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "form_id", referencedColumnName = "id", updatable = false, nullable = false),
            @JoinColumn(name = "form_revision_no", referencedColumnName = "revisionNo", updatable = false, nullable = false)
    })
    private Form form;

    @Column(nullable = false)
    private StatusType status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account submittedBy;

}
