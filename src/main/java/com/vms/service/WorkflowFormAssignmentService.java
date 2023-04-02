package com.vms.service;

import com.vms.dto.AccountDto;
import com.vms.dto.FormRequestDto;
import com.vms.dto.WorkflowFormAssignmentResponseDto;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.WorkflowFormAssignment;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.WorkflowFormAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkflowFormAssignmentService {
    @Autowired
    private WorkflowFormAssignmentRepository workflowFormAssignmentRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FormService formService;

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

    public List<WorkflowFormAssignmentResponseDto> findWorkflowFormAssignmentsNotInFormSubmissionByAccountId(Long accountId)  {
        List<WorkflowFormAssignment> workflowFormAssignments = workflowFormAssignmentRepository.findUnsubmittedWorkflowFormAssignmentsByAccountId(accountId);
        List<WorkflowFormAssignmentResponseDto> response = new ArrayList<>();
        for (WorkflowFormAssignment workflowFormAssignment : workflowFormAssignments) {
            FormCompositeKey fck = new FormCompositeKey(workflowFormAssignment.getForm().getId().getId(), workflowFormAssignment.getForm().getId().getRevisionNo());
            FormRequestDto formId = FormRequestDto.builder()
                            .id(workflowFormAssignment.getForm().getId().getId())
                                    .revisionNo(workflowFormAssignment.getForm().getId().getRevisionNo())
                                            .build();
            Form form = formService.getFormByFck(fck);
            response.add(WorkflowFormAssignmentResponseDto.builder()
                    .account(accountService.getAccountDto(workflowFormAssignment.getAccount()))
                    .workflowId(workflowFormAssignment.getWorkflow().getId())
                    .formId(formId)
                    .formName(form.getName())
                    .formIsFinal(form.isFinal())
                    .build());
        }
        return response;
    }

    public List<WorkflowFormAssignmentResponseDto> findWorkflowFormAssignmentsNotInFormSubmission()  {
        List<WorkflowFormAssignment> workflowFormAssignments = workflowFormAssignmentRepository.findUnsubmittedWorkflowFormAssignments();
        List<WorkflowFormAssignmentResponseDto> response = new ArrayList<>();
        for (WorkflowFormAssignment workflowFormAssignment : workflowFormAssignments) {
            FormCompositeKey fck = new FormCompositeKey(workflowFormAssignment.getForm().getId().getId(), workflowFormAssignment.getForm().getId().getRevisionNo());
            FormRequestDto formId = FormRequestDto.builder()
                    .id(workflowFormAssignment.getForm().getId().getId())
                    .revisionNo(workflowFormAssignment.getForm().getId().getRevisionNo())
                    .build();
            Form form = formService.getFormByFck(fck);
            response.add(WorkflowFormAssignmentResponseDto.builder()
                    .account(accountService.getAccountDto(workflowFormAssignment.getAccount()))
                    .workflowId(workflowFormAssignment.getWorkflow().getId())
                    .formId(formId)
                    .formName(form.getName())
                    .formIsFinal(form.isFinal())
                    .build());
        }
        return response;
    }
}
