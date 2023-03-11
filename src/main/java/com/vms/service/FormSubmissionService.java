package com.vms.service;

import com.vms.dto.*;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.FormSubmission;
import com.vms.model.Workflow;
import com.vms.model.enums.AccountType;
import com.vms.model.enums.StatusType;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.FormSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormSubmissionService {
    @Autowired
    private FormSubmissionRepository formSubmissionRepository;
    @Autowired
    private FormService formService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AccountService accountService;

    public void createFormSubmission(FormSubmissionDto request) {
        Workflow workflow = workflowService.getWorkflowById(request.getWorkflowId());
        Form form = formService.getFormByFck(request.getFck());
        Account account = accountService.getAccountById(request.getAccountId());
        AccountType accountType = account.getAccountType();
        StatusType status;
        if (accountType == AccountType.VENDOR) {
            status = StatusType.AWAITING_ADMIN;
        } else if (accountType == AccountType.ADMIN){
            status = StatusType.AWAITING_APPROVER;
        } else {
            throw new IllegalArgumentException("Forms cannot be submitted by Approver");
        }

        FormSubmission formSubmission = FormSubmission.builder()
                .workflow(workflow)
                .form(form)
                .status(status)
                .submittedBy(account)
                .build();

        formSubmissionRepository.save(formSubmission);
    }

    public void updateFormSubmissionStatus(Long formSubmissionId, StatusType statusType) {
        FormSubmission formSubmission = getFormSubmissionById(formSubmissionId);
        formSubmission.setStatus(statusType);
        formSubmissionRepository.save(formSubmission);
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsByWorkflowAndForm(Long workflowId, FormCompositeKey fck) {
        return generateFsrDto(workflowId, fck);
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsByStatus(StatusType status) {
        return generateFsrDto(status);
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsBySubmitter(Long accountId) {
        return generateFsrDto(accountId);
    }

    private List<FormSubmissionResponseDto> generateFsrDto(Long workflowId, FormCompositeKey fck) {
        Workflow workflow = workflowService.getWorkflowById(workflowId);
        Form form = formService.getFormByFck(fck);
        return generate(formSubmissionRepository.findByWorkflowAndForm(workflow, form));
    }

    private List<FormSubmissionResponseDto> generateFsrDto(StatusType status) {
        return generate(formSubmissionRepository.findByStatus(status));
    }

    private List<FormSubmissionResponseDto> generateFsrDto(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return generate(formSubmissionRepository.findBySubmittedBy(account));
    }

    private List<FormSubmissionResponseDto> generate(List<FormSubmission> formSubmissions){
        List<FormSubmissionResponseDto> fsrDtoList = new ArrayList<>();

        for (FormSubmission formSubmission : formSubmissions) {

            // Workflow Response Dto
            Workflow workflow = formSubmission.getWorkflow();
            FormSubmissionWorkflowDto formSubmissionWorkflowDto = FormSubmissionWorkflowDto.builder()
                    .id(workflow.getId())
                    .name(workflow.getName())
                    .progress(workflow.getProgress())
                    .isFinal(workflow.isFinal())
                    .approvalSequence(workflow.getApprovalSequence())
                    .build();

            // Form Response Dto
            Form form = formSubmission.getForm();
            FormSubmissionFormDto formSubmissionFormDto = FormSubmissionFormDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .build();

            // Account Dto
            Account account = formSubmission.getSubmittedBy();
            AccountDto accountDto = AccountDto.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .email(account.getEmail())
                    .accountType(account.getAccountType())
                    .build();

            FormSubmissionResponseDto fsr = FormSubmissionResponseDto.builder()
                    .id(formSubmission.getId())
                    .workflow(formSubmissionWorkflowDto)
                    .form(formSubmissionFormDto)
                    .status(formSubmission.getStatus())
                    .submittedBy(accountDto)
                    .build();
            fsrDtoList.add(fsr);
        }
        return fsrDtoList;
    }

    public FormSubmission getFormSubmissionById(Long id){
        return formSubmissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Form submission not found"));
    }
}