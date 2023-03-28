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

    @Autowired
    private FormService formService;

    public Long createWorkflow(){
        Workflow workflow = Workflow.builder()
                .name("Untitled Workflow")
                .progress(0)
                .isFinal(false)
                .approvalSequence(new ArrayList<>())
                .build();
        workflowRepository.save(workflow);
        return workflow.getId();
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
    public void updateWorkflow(Long id, WorkflowDto request){
        Workflow workflow = getWorkflowById(id);
        workflow.setName(request.getName());
        workflow.setFinal(request.isFinal());
        workflowRepository.save(workflow);
    }

    public List<WorkflowResponseDto> getWorkflowDtoByAccountId(Long accountId) {
        List<Workflow> workflows = workflowRepository.getWorkflowByAuthorizedUser(accountId);
        List<WorkflowResponseDto> workflowResponseDtos = new ArrayList<>();

        for (Workflow workflow : workflows){
            List<WorkflowFormDto> workflowFormDtos = new ArrayList<>();
            Set<Form> forms = workflow.getForms();
            for (Form form: forms) {
                FormCompositeKey fck = form.getId();
                WorkflowFormDto workflowFormDto = WorkflowFormDto.builder()
                        .formId(fck.getId())
                        .revisionNo(fck.getRevisionNo())
                        .name(form.getName())
                        .description(form.getDescription())
                        .build();
                workflowFormDtos.add(workflowFormDto);
            }
            WorkflowResponseDto workflowResponseDto = WorkflowResponseDto.builder()
                    .id(workflow.getId())
                    .name(workflow.getName())
                    .progress(workflow.getProgress())
                    .isFinal(workflow.isFinal())
                    .forms(workflowFormDtos)
                    .authorizedAccounts(null)
                    .authorizedAccountIds(accountService.getAccountIds(workflow.getAuthorizedAccounts()))
                    .approvalSequence(workflow.getApprovalSequence())
                    .build();

            workflowResponseDtos.add(workflowResponseDto);
        }
        return workflowResponseDtos;
    }

    public WorkflowResponseDto getWorkflowDtoById(Long id){
        Workflow workflow = getWorkflowById(id);
        Set<Form> forms = workflow.getForms();
        List<WorkflowFormDto> workflowForms = new ArrayList<>();
        for (Form form: forms) {
            FormCompositeKey fck = form.getId();
            WorkflowFormDto workflowForm = WorkflowFormDto.builder()
                    .formId(fck.getId())
                    .revisionNo(fck.getRevisionNo())
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
                .authorizedAccounts(accountService.getAccountDtoList(workflow.getAuthorizedAccounts()))
                .authorizedAccountIds(accountService.getAccountIds(workflow.getAuthorizedAccounts()))
                .approvalSequence(workflow.getApprovalSequence())
                .build();
    }

    public List<WorkflowResponseDto> getWorkflowDtoList(){
        Iterable<Workflow> workflows = workflowRepository.findAll();
        List<WorkflowResponseDto> temp = new ArrayList<>();
        for(Workflow workflow : workflows){
            temp.add(WorkflowResponseDto.builder()
            .id(workflow.getId())
            .name(workflow.getName())
            .progress(workflow.getProgress())
            .isFinal(workflow.isFinal())
            .build());
        }
        return temp;
    }


    public Workflow getWorkflowById(Long id){
        return workflowRepository.findById(id).orElseThrow(() -> new RuntimeException("Workflow not found"));
    }

    public void addFormToWorkflow(FormCompositeKey fck, Long id){
        Workflow workflow = getWorkflowById(id);
        if(workflow.isFinal()){
            throw new RuntimeException("Workflow is final and cannot be edited");
        }
        Form form = formService.getFormByFck(fck);

        if (!form.isFinal()){
            throw new RuntimeException("Form must be final before it can be added");
        }

        List<Long> newApprovalSequence = workflow.getApprovalSequence();
        newApprovalSequence.add(fck.getId());
        workflow.setApprovalSequence(newApprovalSequence);

        Set<Form> newForms = workflow.getForms();
        newForms.add(form);
        workflow.setForms(newForms);

        workflowRepository.save(workflow);
    }

    public void removeFormFromWorkflow(FormCompositeKey fck, Long id){
        Workflow workflow = getWorkflowById(id);
        if(workflow.isFinal()){
            throw new RuntimeException("Workflow is final and cannot be edited");
        }
        Form form = formService.getFormByFck(fck);

        List<Long> currentApprovalSequence = workflow.getApprovalSequence();
        List<Long> newApprovalSequence = new ArrayList<>();
        for(Long sequence: currentApprovalSequence){
            if(!fck.getId().equals(sequence)){
                newApprovalSequence.add(sequence);
            }
        }
        workflow.setApprovalSequence(newApprovalSequence);

        Set<Form> newForms = workflow.getForms();
        newForms.remove(form);
        workflow.setForms(newForms);

        workflowRepository.save(workflow);
    }
}
