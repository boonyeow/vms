package com.vms.model;

import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="submission")
public class FormSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "form_id")
    private Form form;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Account submitter;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_submission_id")
    private List<Field> fields;

    @Column(name = "submission_date")
    private Date submissionDate;

    public FormSubmission(Form form, Account submitter, List<Field> fields, Date submissionDate) {
        this.form = form;
        this.submitter = submitter;
        this.fields = fields;
        this.submissionDate = submissionDate;
    }

    public Long getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public List<Field> getFields() {
        return fields;
    }
}
