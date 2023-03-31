package com.vms.service;

import com.vms.dto.*;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
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

    public void removeWorkflow(Long workflowId){
        Workflow workflow = getWorkflowById(workflowId);
        if(workflow.isFinal()){
            throw new RuntimeException("Final workflow cannot be deleted.");
        }
        System.out.println("hehehe");
        workflowRepository.delete(workflow);
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
        List<Form> newForms = new ArrayList<>();

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
                            .account(accountService.getAccountFromDto(wfaDto.getAccount()))
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

    public WorkflowResponseDto getWorkflowDtoById(Long id){
        Workflow workflow = getWorkflowById(id);
        return getWorkflowResponseDto(workflow);
    }

    private WorkflowResponseDto getWorkflowResponseDto(Workflow workflow) {
        List<Form> forms = workflow.getForms();
        List<WorkflowFormDto> workflowForms = new ArrayList<>();

        for(Form form : forms){
            Account assignedUser = workflowFormAssignmentService.findAssignedUser(form, workflow);
            AccountDto accountDto = (assignedUser == null) ? null : accountService.getAccountDto(assignedUser);
            workflowForms.add(
            WorkflowFormDto.builder()
                    .formId(form.getId())
                    .account(accountDto)
                    .name(form.getName())
                    .description(form.getDescription())
                    .build());
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


    public List<WorkflowResponseDto> getWorkflowDtoList(){
        Iterable<Workflow> workflows = workflowRepository.findAll();
        List<WorkflowResponseDto> temp = new ArrayList<>();

        for(Workflow workflow : workflows){
            temp.add(getWorkflowResponseDto(workflow));
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

        List<Form> newForms = workflow.getForms();
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

        List<Form> newForms = workflow.getForms();
        newForms.remove(form);
        workflow.setForms(newForms);

        workflowRepository.save(workflow);
    }
}
