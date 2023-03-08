package com.vms.service;
import com.vms.model.*;
import com.vms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormSubmissionService {
    @Autowired
    private FormSubmissionRepository formSubmissionRepository;

    public FormSubmission saveFormSubmission(FormSubmission formSubmission) {
        return formSubmissionRepository.save(formSubmission);
    }

    public FormSubmission getFormSubmissionById(Long id) {
        return formSubmissionRepository.findById(id).orElse(null);
    }

    public List<FormSubmission> getAllFormSubmissions() {
        return formSubmissionRepository.findAll();
    }

    public void deleteFormSubmission(Long id) {
        formSubmissionRepository.deleteById(id);
    }
}
