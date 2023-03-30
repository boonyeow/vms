package com.vms.service;

import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.WorkflowFormAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkflowFormAssignmentService {
    @Autowired
    private WorkflowFormAssignmentRepository workflowFormAssignmentRepository;

    public void updateWorkflowFormAssignment(Workflow workflow, List<WorkflowFormAssignment> workflowFormAssignments){
//        List<WorkflowFormAssignment> updated = new ArrayList<>();
        workflowFormAssignmentRepository.deleteByWorkflow(workflow);
        workflowFormAssignmentRepository.saveAll(workflowFormAssignments);
    }

    public void deleteByWorkflow(Workflow workflow){
        System.out.println("Before");
        System.out.println(workflow.getApprovalSequence());
        workflowFormAssignmentRepository.deleteByWorkflow(workflow);
        System.out.println("After");
        System.out.println(workflow.getApprovalSequence());
    }
}
