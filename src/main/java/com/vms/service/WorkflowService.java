package com.vms.service;

import com.vms.dto.*;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import com.vms.model.enums.AccountType;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkflowService {
    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FormService formService;

    @Autowired
    private WorkflowFormAssignmentService workflowFormAssignmentService;

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

    public void updateWorkflowEntirely(Long id, WorkflowUpdateDto request){
        Workflow workflow = getWorkflowById(id);

        if (workflow.isFinal()) {
            throw new RuntimeException("Workflow is final and cannot be updated");
        }

        workflow.setName(request.getName());
        workflow.setFinal(request.isFinal());

        List<Long> newApprovalSequence = new ArrayList<>();
        Set<Form> newForms = new HashSet<>();

        System.out.println("HELLO");
        List<WorkflowFormAssignment> workflowFormAssignments = new ArrayList<>();
        for (WorkflowFormAssignmentDto wfaDto : request.getWorkflowFormAssignments()) {
            FormCompositeKey fck = new FormCompositeKey(wfaDto.getFormId().getId(), wfaDto.getFormId().getRevisionNo());
            Form form = formService.getFormByFck(fck);

            if (!form.isFinal()){
                throw new RuntimeException("Form must be final before it can be added");
            }

            workflowFormAssignments.add(WorkflowFormAssignment.builder()
                            .workflow(workflow)
                            .form(form)
                            .account(accountService.getAccountById(wfaDto.getAccountId()))
                            .build());

            newApprovalSequence.add(fck.getId());
            newForms.add(form);
        }
        System.out.println("HELLO");
        workflow.setApprovalSequence(newApprovalSequence);
        workflow.setForms(newForms);
        workflow.setWorkflowFormAssignments(workflowFormAssignments);
        workflowFormAssignmentService.updateWorkflowFormAssignment(workflow, workflowFormAssignments);
        workflowRepository.save(workflow);
    }

    public List<WorkflowAccountTypeDto> getWorkflowDtoByAccountType(AccountType accountType) {
        List<Workflow> workflows = workflowRepository.getByFinalAndAuthorizedAccounts(accountType);
        List<WorkflowAccountTypeDto> filteredWorkflows = new ArrayList<>();

        for (Workflow workflow : workflows) {
            // Inside each workflow, check each form for its Assigned account and check if its Vendor
            Map<Long, List<FormDetailsDto>> formsAssignedToAccountType = new HashMap<>();
            List<WorkflowFormDto> workflowFormDtos = new ArrayList<>();

            for (Form form : workflow.getForms()){
                workflowFormDtos.add( WorkflowFormDto.builder()
                        .formId(form.getId().getId())
                        .revisionNo(form.getId().getRevisionNo())
                        .name(form.getName())
                        .description(form.getDescription())
                        .build());

                for (Account account : form.getAuthorizedAccounts()) {
                    // if it is Vendor, assign to List
                    if (account.getAccountType().equals(accountType)) {
                        if (formsAssignedToAccountType.containsKey(account.getId())) {
                            List<FormDetailsDto> currentList = formsAssignedToAccountType.get(account.getId());
                            currentList.add(FormDetailsDto.builder()
                                            .form_id(form.getId())
                                            .email(account.getEmail())
                                            .name(form.getName())
                                            .company(account.getCompany())
                                            .build());
                            formsAssignedToAccountType.put(account.getId(), currentList);
                        } else {
                            List<FormDetailsDto> fckList = new ArrayList<>();
                            fckList.add(FormDetailsDto.builder()
                                    .form_id(form.getId())
                                    .email(account.getEmail())
                                    .name(form.getName())
                                    .company(account.getCompany())
                                    .build());
                            formsAssignedToAccountType.put(account.getId(), fckList);
                        }

                    }
                }
            }

            List<AccountDto> accountDtos = new ArrayList<>();

            for (Account account : workflow.getAuthorizedAccounts()) {
                accountDtos.add(AccountDto.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .email(account.getEmail())
                        .company(account.getCompany())
                        .accountType(account.getAccountType())
                        .build());
            }

            WorkflowAccountTypeDto filteredWorkflow = WorkflowAccountTypeDto.builder()
                    .id(workflow.getId())
                    .name(workflow.getName())
                    .progress(workflow.getProgress())
                    .isFinal(workflow.isFinal())
                    .forms(workflowFormDtos)
                    .authorizedAccounts(accountDtos)
                    .formsAssignedToRequestedAccountType(formsAssignedToAccountType)
                    .build();

            filteredWorkflows.add(filteredWorkflow);
        }
        return filteredWorkflows;
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
