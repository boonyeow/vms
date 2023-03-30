package com.vms.repository;

import com.vms.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowFormAssignmentRepository extends CrudRepository<WorkflowFormAssignment, Long> {
    List<WorkflowFormAssignment> deleteByWorkflow(Workflow workflow);

    Optional<WorkflowFormAssignment> findByFormAndWorkflow(Form form, Workflow workflow);
}

