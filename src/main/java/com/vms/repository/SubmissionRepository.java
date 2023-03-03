package com.vms.repository;
import com.vms.model.FormSubmission;
import org.springframework.data.repository.CrudRepository;

public interface SubmissionRepository extends CrudRepository<FormSubmission, Long> {
}