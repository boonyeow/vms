package com.vms.repository;

import com.vms.model.Field;
import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowFormAssignmentRepository extends CrudRepository<WorkflowFormAssignment, Long> {
    List<Workflow> deleteByWorkflow(Workflow workflow);
}

