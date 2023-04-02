package com.vms.repository;

import com.vms.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowFormAssignmentRepository extends CrudRepository<WorkflowFormAssignment, Long> {
    List<WorkflowFormAssignment> deleteByWorkflow(Workflow workflow);

    Optional<WorkflowFormAssignment> findByFormAndWorkflow(Form form, Workflow workflow);

    @Query("SELECT wfa FROM WorkflowFormAssignment wfa " +
            "LEFT JOIN FormSubmission fs ON wfa.form.id = fs.form.id " +
            "AND wfa.form.id.revisionNo = fs.form.id.revisionNo " +
            "AND wfa.workflow.id = fs.workflow.id " +
            "AND fs.status != 'DRAFT'" +
            "AND fs.submittedBy.id = :accountId " +
            "WHERE fs.id IS NULL AND wfa.account.id = :accountId")
    List<WorkflowFormAssignment> findUnsubmittedWorkflowFormAssignmentsByAccountId(Long accountId);

    @Query("SELECT wfa FROM WorkflowFormAssignment wfa " +
            "LEFT JOIN FormSubmission fs ON wfa.form.id = fs.form.id " +
            "AND wfa.form.id.revisionNo = fs.form.id.revisionNo " +
            "AND wfa.workflow.id = fs.workflow.id " +
            "AND fs.status != 'DRAFT'" +
            "WHERE fs.id IS NULL")
    List<WorkflowFormAssignment> findUnsubmittedWorkflowFormAssignments();


}

