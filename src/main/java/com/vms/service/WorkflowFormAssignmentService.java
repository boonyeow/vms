package com.vms.service;

import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.WorkflowFormAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkflowFormAssignmentService {
    @Autowired
    private WorkflowFormAssignmentRepository workflowFormAssignmentRepository;
    @Autowired
    private AccountService accountService;
    @Transactional
    public void updateWorkflowFormAssignment(Workflow workflow, List<WorkflowFormAssignment> workflowFormAssignments){
        workflowFormAssignmentRepository.deleteByWorkflow(workflow);
        workflowFormAssignmentRepository.saveAll(workflowFormAssignments);
    }

    public Account findAssignedUser(Form form, Workflow workflow){

        Optional<WorkflowFormAssignment> wfa = workflowFormAssignmentRepository.findByFormAndWorkflow(form, workflow);
        if(wfa.isEmpty()){
            return null;
        }
        return wfa.get().getAccount();
    }
}
