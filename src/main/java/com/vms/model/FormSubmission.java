package com.vms.model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.vms.dto.FormSubmissionDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="form_submission")
public class FormSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToOne
    @JoinColumn(name = "submitter_id")
    private Account submitter;

    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submissionDate;

    @OneToMany(mappedBy = "formSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldResponse> fieldResponses;

    public void addFieldResponse(FieldResponse fieldResponse) {
        fieldResponses.add(fieldResponse);
        fieldResponse.setFormSubmission(this);
    }

    public void removeFieldResponse(FieldResponse fieldResponse) {
        fieldResponses.remove(fieldResponse);
        fieldResponse.setFormSubmission(null);
    }

    public List<FieldResponse> getFieldResponses() {
        if (fieldResponses == null) {
            fieldResponses = new ArrayList<>();
        }
        return fieldResponses;
    }

    // public static FormSubmissionDTO toFormSubmissionDTO(FormSubmission formSubmission) {
    //     return new FormSubmissionDTO(
    //         formSubmission.getId(),
    //         formSubmission.getForm(),
    //         formSubmission.getSubmitter().getId(),
    //         formSubmission.getSubmissionDate(),
    //         FieldResponse.toFieldResponseDTOs(formSubmission.getFieldResponses())
    //     );
    // }

    public static FormSubmissionDTO toFormSubmissionDTO(FormSubmission formSubmission) {
        return new FormSubmissionDTO(formSubmission);
    }


}