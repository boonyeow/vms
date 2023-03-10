package com.vms.repository;

import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.FormSubmission;
import com.vms.model.Workflow;
import com.vms.model.enums.StatusType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormSubmissionRepository extends CrudRepository<FormSubmission, Long> {
    List<FormSubmission> findBySubmittedBy(Account account);

    List<FormSubmission> findByStatus(StatusType status);

    List<FormSubmission> findByWorkflowAndForm(Workflow workflow, Form form);

}
