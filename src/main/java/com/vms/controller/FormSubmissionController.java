package com.vms.controller;

import com.vms.model.FormSubmission;
import com.vms.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
public class FormSubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public FormSubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public Iterable<FormSubmission> getAllSubmissions() {
        return submissionService.getAllSubmission();
    }

    @PostMapping
    public ResponseEntity<FormSubmission> submitForm(@RequestBody FormSubmission formSubmission) {
        try {
            FormSubmission newSubmission = submissionService.submitForm(formSubmission);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSubmission);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormSubmission> getFormSubmissionById(@PathVariable Long id) {
        try {
            return ResponseEntity.of(submissionService.getFormSubmissionById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
