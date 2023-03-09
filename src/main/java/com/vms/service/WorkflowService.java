package com.vms.service;

import com.vms.dto.WorkflowDto;
import com.vms.dto.WorkflowFormDto;
import com.vms.dto.WorkflowResponseDto;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WorkflowService {
    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private AccountService accountService;

    public void createWorkflow(){
        Workflow workflow = Workflow.builder()
                .name("Untitled Workflow")
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
        workflow.setFinal(true);
        workflowRepository.save(workflow);
    }
    public void updateWorkflow(WorkflowDto request){
        Workflow workflow = getWorkflowById(request.getId());
        workflow.setName(request.getName());
        workflow.setFinal(request.isFinal());
        workflowRepository.save(workflow);
    }

    public WorkflowResponseDto getWorkflowDtoById(Long id){
        Workflow workflow = getWorkflowById(id);
        List<Form> forms = workflow.getForms();
        List<WorkflowFormDto> workflowForms = new ArrayList<>();
        for (Form form: forms) {
            FormCompositeKey fck = form.getId();
            WorkflowFormDto workflowForm = WorkflowFormDto.builder()
                    .formId(fck.getId())
                    .revisionId(fck.getRevisionNo())
                    .name(form.getName())
                    .description(form.getDescription())
                    .build();
            workflowForms.add(workflowForm);
        }

        return WorkflowResponseDto.builder()
                .id(workflow.getId())
                .name(workflow.getName())
                .progress(workflow.getProgress())
                .isFinal(workflow.isFinal())
                .forms(workflowForms)
                .approvalSequence(workflow.getApprovalSequence())
                .build();
    }

    public Workflow getWorkflowById(Long id){
        return workflowRepository.findById(id).orElseThrow(() -> new RuntimeException("Workflow not found"));
    }
}
