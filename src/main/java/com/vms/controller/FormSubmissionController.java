package com.vms.controller;

import com.vms.dto.FormSubmissionDTO;
import com.vms.model.FormSubmission;
import com.vms.service.FormSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/form-submissions")
public class FormSubmissionController {

    @Autowired
    private FormSubmissionService formSubmissionService;

    @PostMapping
    public ResponseEntity<FormSubmission> createFormSubmission(@RequestBody FormSubmission formSubmission) {
        FormSubmission savedFormSubmission = formSubmissionService.saveFormSubmission(formSubmission);
        return new ResponseEntity<>(savedFormSubmission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormSubmission> getFormSubmissionById(@PathVariable Long id) {
        FormSubmission formSubmission = formSubmissionService.getFormSubmissionById(id);
        if (formSubmission == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(formSubmission, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FormSubmission>> getAllFormSubmissions() {
        List<FormSubmission> formSubmissions = formSubmissionService.getAllFormSubmissions();
        return new ResponseEntity<>(formSubmissions, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormSubmission(@PathVariable Long id) {
        FormSubmission formSubmission = formSubmissionService.getFormSubmissionById(id);
        if (formSubmission == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        formSubmissionService.deleteFormSubmission(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

