package com.vms.model;
import jakarta.persistence.*;

@Entity
@Table(name="field_response")
public class FieldResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_submission_id")
    private FormSubmission formSubmission;

    @Column(name = "response")
    private String response;

    public FieldResponse(Field field, FormSubmission formSubmission, String response) {
        this.field = field;
        this.formSubmission = formSubmission;
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public Field getField() {
        return field;
    }

    public FormSubmission getFormSubmission() {
        return formSubmission;
    }

    public String getResponse() {
        return response;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

