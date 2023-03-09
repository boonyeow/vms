//package com.vms.dto;
//import java.util.List;
//import com.vms.model.FieldResponse;
//import com.vms.model.Form;
//import com.vms.model.Account;
//import lombok.*;
//
//import com.vms.model.FormSubmission;
//
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class FormSubmissionDTO {
//    private Long id;
//    private Form form;
//    private Account submitter;
//    private String submissionDate;
//    private List<FieldResponse> fieldResponses;
//
//    public FormSubmissionDTO(FormSubmission formSubmission) {
//        this.id = formSubmission.getId();
//        this.form = formSubmission.getForm();
//        this.submitter = formSubmission.getSubmitter();
//        this.submissionDate = formSubmission.getSubmissionDate().toString();
//        this.fieldResponses = formSubmission.getFieldResponses();
//    }
//}
