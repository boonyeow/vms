package com.vms.repository;

import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.FormSubmission;
import com.vms.model.Workflow;
import com.vms.model.enums.AccountType;
import com.vms.model.enums.StatusType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormSubmissionRepository extends CrudRepository<FormSubmission, Long> {
    List<FormSubmission> findBySubmittedBy(Account account);

    List<FormSubmission> findByStatus(StatusType status);

    List<FormSubmission> findByWorkflowAndForm(Workflow workflow, Form form);

    @Query("SELECT fs FROM FormSubmission fs WHERE (fs.reviewedByAdmin.id = :accountId) OR (fs.reviewedByApprover.id = :accountId)")
    List<FormSubmission> findAllByReviewerId(@Param("accountId") Long accountId);
}
