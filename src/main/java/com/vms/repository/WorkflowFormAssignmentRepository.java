package com.vms.repository;

import com.vms.model.Field;
import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface WorkflowFormAssignmentRepository extends CrudRepository<WorkflowFormAssignment, Long> {
    void deleteByWorkflow(Workflow workflow);
}

