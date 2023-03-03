package com.vms.service;
import com.vms.model.FormSubmission;
import com.vms.model.CustomResponseStatusException;
import com.vms.repository.SubmissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }
    
    public Iterable<FormSubmission> getAllSubmission(){
        return submissionRepository.findAll();
    }

    public FormSubmission submitForm(FormSubmission formSubmission) {
        // Check if the FormSubmission already exists
        // if (submissionRepository.existsById(formSubmission.getId())) {
        //     throw new CustomResponseStatusException(HttpStatus.CONFLICT, "FormSubmission already exists with id " + formSubmission.getId());
        // }
        
        return submissionRepository.save(formSubmission);
    }

    public Optional<FormSubmission> getFormSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }
}
