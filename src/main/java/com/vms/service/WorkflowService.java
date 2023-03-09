package com.vms.service;

import com.vms.dto.WorkflowDto;
import com.vms.model.Account;
import com.vms.model.Workflow;
import com.vms.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class WorkflowService {
    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private AccountService accountService;

    public void createWorkflow(WorkflowDto request){
        Workflow workflow = Workflow.builder()
                .name(request.getName())
                .progress(0)
                .isFinal(false)
                .approvalSequence(new ArrayList<>())
                .build();
        workflowRepository.save(workflow);
    }

    public void addAuthorizedAccount(Long workflowId, Long accountId){
        Workflow workflow = getWorkflowById(workflowId);
        Account account = accountService.getAccountById(accountId);

        Set<Account> authorizedAccounts = workflow.getAuthorizedAccounts();
        if(authorizedAccounts.contains(account)){
            throw new RuntimeException("Account has already been assigned workflow id " + workflowId);
        }
        authorizedAccounts.add(account);
        workflow.setAuthorizedAccounts(authorizedAccounts);
        workflowRepository.save(workflow);
    }

    public void removeAuthorizedAccount(Long workflowId, Long accountId){
        Workflow workflow = getWorkflowById(workflowId);
        Account account = accountService.getAccountById(accountId);

        Set<Account> authorizedAccounts = workflow.getAuthorizedAccounts();
        if(!authorizedAccounts.contains(account)){
            throw new RuntimeException("Account cannot be found in workflow");
        }
        authorizedAccounts.remove(account);
        workflow.setAuthorizedAccounts(authorizedAccounts);
        workflowRepository.save(workflow);
    }

    public void publishWorkflow(Long workflowId){
        Workflow workflow = getWorkflowById(workflowId);
        workflow.setIsFinal(true);
        workflowRepository.save(workflow);
    }

    public Workflow getWorkflowById(Long id){
        return workflowRepository.findById(id).orElseThrow(() -> new RuntimeException("Workflow not found"));
    }


}
