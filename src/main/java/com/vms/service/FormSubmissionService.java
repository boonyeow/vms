package com.vms.service;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import com.vms.dto.*;
import com.vms.exception.FormSubmissionImmutableException;
import com.vms.exception.FormSubmissionNotFoundException;
import com.vms.exception.InvalidSequenceException;
import com.vms.model.*;
import com.vms.model.enums.AccountType;
import com.vms.model.enums.StatusType;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.FormSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    @Autowired
    private FieldService fieldService;

    public void createFormSubmission(FormSubmissionDto request) {
        Workflow workflow = workflowService.getWorkflowById(request.getWorkflowId());
        Form form = formService.getFormByFck(request.getFck());
        Account account = accountService.getAccountById(request.getAccountId());
        AccountType accountType = account.getAccountType();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        Map<Long, String> fieldResponses = request.getFieldResponses();
        StatusType status;
        if (request.getStatus() != StatusType.DRAFT) {
            if (accountType == AccountType.VENDOR) {
                status = StatusType.AWAITING_ADMIN;
            } else if (accountType == AccountType.ADMIN) {
                status = StatusType.AWAITING_APPROVER;
            } else {
                throw new IllegalArgumentException("Forms cannot be submitted by Approver");
            }
        } else {
            status = StatusType.DRAFT;
        }
        FormSubmission formSubmission = FormSubmission.builder()
                .workflow(workflow)
                .form(form)
                .status(status)
                .submittedBy(account)
                .fieldResponses(fieldResponses)
                .dateOfSubmission(dtf.format(now))
                .build();

        formSubmissionRepository.save(formSubmission);
    }

    public void updateFormSubmission(Long formSubmissionId, FormSubmissionDto request) {
        FormSubmission formSubmission = getFormSubmissionById(formSubmissionId);

        Workflow workflow = workflowService.getWorkflowById(request.getWorkflowId());
        Form form = formService.getFormByFck(request.getFck());
        Account account = accountService.getAccountById(request.getAccountId());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        formSubmission.setForm(form);
        formSubmission.setWorkflow(workflow);
        formSubmission.setSubmittedBy(account);
        formSubmission.setDateOfSubmission(dtf.format(now));

        if (formSubmission.getStatus() == StatusType.DRAFT) {
            formSubmission.setFieldResponses(request.getFieldResponses());
        } else {
            throw new FormSubmissionImmutableException("Form Submission has been submitted and cannot be changed");
        }

        formSubmission.setStatus(request.getStatus());

        formSubmissionRepository.save(formSubmission);
    }

    public void updateFormSubmissionStatus(Long formSubmissionId, StatusType statusType) {
        FormSubmission formSubmission = getFormSubmissionById(formSubmissionId);
        List<FormCompositeKey> approvalSequence = formSubmission.getWorkflow().getApprovalSequence();
        for (FormCompositeKey fck : approvalSequence) {
            // if loop reaches fck without throwing error, it means the review is done in order
            if ((fck.getId() == formSubmission.getForm().getId().getId()) && (fck.getRevisionNo() == formSubmission.getForm().getId().getRevisionNo())) {
                break;
            } else {
                Form form = formService.getFormByFck(fck);
                List<FormSubmission> fsr = formSubmissionRepository.findByWorkflowAndForm(formSubmission.getWorkflow(), form);
                if ((fsr.get(0).getStatus() != StatusType.APPROVED)) {
                    throw new InvalidSequenceException("Forms must be reviewed and approved in the sequence specified");
                }
            }
        }
        formSubmission.setStatus(statusType);
        formSubmissionRepository.save(formSubmission);
    }

    public List<FormSubmissionResponseDto> getAllFormSubmissions() {
        Iterable<FormSubmission> formSubmissionIterables = formSubmissionRepository.findAll();
        List<FormSubmission> formSubmissions = StreamSupport.stream(formSubmissionIterables.spliterator(), false)
                .collect(Collectors.toList());
        return generateFsrDto(formSubmissions);
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsById(Long formSubmissionId) {
        Optional<FormSubmission> optionalFormSubmission = formSubmissionRepository.findById(formSubmissionId);
        List<FormSubmission> formSubmissions = new ArrayList<>();

        if (optionalFormSubmission.isPresent()) {
            FormSubmission formSubmission = optionalFormSubmission.get();
            formSubmissions.add(formSubmission);
        }
        return generateFsrDto(formSubmissions);
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsByWorkflowAndForm(Long workflowId, FormCompositeKey fck) {
        Workflow workflow = workflowService.getWorkflowById(workflowId);
        Form form = formService.getFormByFck(fck);
        return generateFsrDto(formSubmissionRepository.findByWorkflowAndForm(workflow, form));
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsByStatus(StatusType status) {
        return generateFsrDto(formSubmissionRepository.findByStatus(status));
    }

    public List<FormSubmissionResponseDto> getFormSubmissionsBySubmitter(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return generateFsrDto(formSubmissionRepository.findBySubmittedBy(account));
    }

    private List<FormSubmissionResponseDto> generateFsrDto(List<FormSubmission> formSubmissions){
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

            // Field Response Dto for Form Response Dto
            List<Field> fields = form.getFields();
            List<FormSubmissionFieldDto> formSubmissionFieldDtoList = new ArrayList<>();
            for (Field field: fields) {
                FormSubmissionFieldDto formSubmissionFieldDto = FormSubmissionFieldDto.builder()
                        .id(field.getId())
                        .name(field.getName())
                        .helpText(field.getHelpText())
                        .nextFieldsId(getNextFieldsIdFromMap(field.getOptions()))
                        .build();
                formSubmissionFieldDtoList.add(formSubmissionFieldDto);
            }

            FormSubmissionFormDto formSubmissionFormDto = FormSubmissionFormDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .fields(formSubmissionFieldDtoList)
                    .build();

            // Account Dto
            Account account = formSubmission.getSubmittedBy();
            AccountDto accountDto = AccountDto.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .email(account.getEmail())
                    .company(account.getCompany())
                    .accountType(account.getAccountType())
                    .build();

            FormSubmissionResponseDto fsr = FormSubmissionResponseDto.builder()
                    .id(formSubmission.getId())
                    .workflow(formSubmissionWorkflowDto)
                    .form(formSubmissionFormDto)
                    .status(formSubmission.getStatus())
                    .submittedBy(accountDto)
                    .fieldResponses(formSubmission.getFieldResponses())
                    .dateOfSubmission(formSubmission.getDateOfSubmission())
                    .build();
            fsrDtoList.add(fsr);
        }
        return fsrDtoList;
    }

    public FormSubmission getFormSubmissionById(Long id){
        return formSubmissionRepository.findById(id).orElseThrow(() -> new FormSubmissionNotFoundException("Form submission not found"));
    }

    private Map<String, Long> getNextFieldsIdFromMap(Map<String, Field> nextFieldsMap){
        Map<String, Long> nextFieldsId = new HashMap<>();
        for(Map.Entry<String, Field> entry : nextFieldsMap.entrySet()){
            nextFieldsId.put(entry.getKey(), entry.getValue().getId());
        }
        return nextFieldsId;
    }
}