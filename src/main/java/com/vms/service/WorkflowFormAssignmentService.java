package com.vms.service;

import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.WorkflowFormAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowFormAssignmentService {
    @Autowired
    private WorkflowFormAssignmentRepository workflowFormAssignmentRepository;
    @Autowired
    private AccountService accountService;

    public void updateWorkflowFormAssignment(Workflow workflow, List<WorkflowFormAssignment> workflowFormAssignments){
        workflowFormAssignmentRepository.deleteByWorkflow(workflow);
        workflowFormAssignmentRepository.saveAll(workflowFormAssignments);
    }
}
