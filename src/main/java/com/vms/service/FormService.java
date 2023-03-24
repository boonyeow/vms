package com.vms.service;

import com.vms.dto.*;
import com.vms.model.Account;
import com.vms.model.Field;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormService {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private AccountService accountService;

    @Autowired
    private FieldService fieldService;

     public Form createForm(){
         FormCompositeKey fck = FormCompositeKey.builder().revisionNo(1).build();
         Form form = Form.builder()
                 .id(fck)
                 .name("Untitled Form")
                 .description("")
                 .isFinal(false)
                 .authorizedAccounts(new ArrayList<>())
                 .workflows(new HashSet<>())
                 .fields(new ArrayList<>())
                 .build();
         formRepository.saveWithAutoGeneratedId(form);
         return form;
     }

    public Form duplicateForm(FormCompositeKey fck){
         Form form = getFormByFck(fck);
         Integer currentRevisionNo = formRepository.findByFormId_Id(fck.getId()).stream()
                 .mapToInt(f -> f.getId().getRevisionNo())
                 .max()
                 .orElseThrow(() -> new RuntimeException("surprise"));
         fck.setRevisionNo(currentRevisionNo + 1);
         Form duplicatedForm = Form.builder()
                 .id(fck)
                 .name(form.getName() + " Copy")
                 .description(form.getDescription())
                 .isFinal(false)
                 .authorizedAccounts(form.getAuthorizedAccounts())
                 .workflows(new HashSet<>())
                 .fields(form.getFields())
                 .build();
         formRepository.save(duplicatedForm);
         return duplicatedForm;
    }

    public void updateForm(FormCompositeKey fck, FormDto request, boolean applyChanges){
         Form form = getFormByFck(fck);
         form.setName(request.getName());
         form.setDescription(request.getDescription());
         form.setFinal(request.isFinal());
         List<FieldRequestDto> fieldRequestDtoList = request.getFields();

         List<Field> fields = new ArrayList<>();
         for(FieldRequestDto fieldRequestDto: fieldRequestDtoList){
             Field field = fieldService.createField(fieldRequestDto, form);
             fields.add(field);
         }

        form.setFields(fields);

         if(applyChanges){
             applyChangesToAllWorkflows(fck, form);
         }

         formRepository.save(form);
    }

    private void applyChangesToAllWorkflows(FormCompositeKey fck, Form form){
        // Fetch existing revision and remove all associated workflow
        FormCompositeKey existingFck = new FormCompositeKey(fck.getId(), fck.getRevisionNo() - 1);
        Form existingForm = getFormByFck(existingFck);
        Set<Workflow> associatedWorkflows = new HashSet<>();
        associatedWorkflows.addAll(existingForm.getWorkflows());
        existingForm.setWorkflows(new HashSet<>());

        // Add removed associated workflows to latest revision
        form.setWorkflows(associatedWorkflows);
        formRepository.save(existingForm);
    }

     public void updateFormAuthorizedAccounts(FormCompositeKey fck, List<String> emails){
        Form form = getFormByFck(fck);
         List<Account> authorizedAccounts = new ArrayList<>();
         for (String email: emails) {
             Account account = accountService.getAccountByEmail(email)
                     .orElseThrow(() -> new ResourceNotFoundException("Account " + email + " not found"));
             authorizedAccounts.add(account);
         }
         form.setAuthorizedAccounts(authorizedAccounts);
         formRepository.saveWithAutoGeneratedId(form);
     }

     // Remember to test it to see if deleting form affects FormSubmission and Workflow
    public void deleteForm(FormCompositeKey fck){
         Form form = getFormByFck(fck);
         if(!form.getWorkflows().isEmpty()){
             throw new RuntimeException("Unable to delete form due to referential violation");
         }
        formRepository.delete(form);
    };

     // Please check and confirm thank you very much i want go lie down liao
     public List<FormResponseDto> getAllFormDto(){
         Iterable<Form> forms = formRepository.findAll();
         List<FormResponseDto> formResponses = new ArrayList<>();
         List<AccountDto> authorizedAccounts = new ArrayList<>();
         for (Form form: forms){
             List<AccountDto> accountDtoList = accountService.getAccountDtoList(form.getAuthorizedAccounts());
             for (AccountDto accountDto: accountDtoList){
                authorizedAccounts.add(accountDto);
             }
             List<Long> workflowIds = getWorkflowIds(form.getWorkflows());

            FormResponseDto formResponseDto = FormResponseDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .authorizedAccounts(authorizedAccounts)
                    .workflows(workflowIds)
                    .build();
             formResponses.add(formResponseDto);
         }
         return formResponses;
     }

     public List<Long> getWorkflowIds(Set<Workflow> workflows){
         List<Long> workflowIds = new ArrayList<>();
         for(Workflow workflow : workflows){
             workflowIds.add(workflow.getId());
         }
         return workflowIds;
     }

    public List<FormResponseDto> getFormDtoByState(Boolean state){
        Iterable<Form> forms = formRepository.findFormByState(state);
        List<FormResponseDto> formResponses = new ArrayList<>();
        List<AccountDto> authorizedAccounts = new ArrayList<>();
        for (Form form: forms){
            List<AccountDto> accountDtoList = accountService.getAccountDtoList(form.getAuthorizedAccounts());
            for (AccountDto accountDto: accountDtoList){
                authorizedAccounts.add(accountDto);
            }
            List<Long> workflowIds = getWorkflowIds(form.getWorkflows());
            FormResponseDto formResponseDto = FormResponseDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .authorizedAccounts(authorizedAccounts)
                    .workflows(workflowIds)
                    .build();
            formResponses.add(formResponseDto);
        }
        return formResponses;
    }

    public FormResponseDto getFormDtoByFck(FormCompositeKey fck){
        Form form = getFormByFck(fck);

        List<Long> workflowIds = getWorkflowIds(form.getWorkflows());
        return FormResponseDto.builder()
                .id(form.getId())
                .name(form.getName())
                .description(form.getDescription())
                .isFinal(form.isFinal())
                .authorizedAccounts(accountService.getAccountDtoList(form.getAuthorizedAccounts()))
                .workflows(workflowIds)
                .build();
    }

    public Form getFormByFck(FormCompositeKey fck){
        return formRepository.findById(fck)
                .orElseThrow(() -> new RuntimeException("Form not found")
                );
    }

    public List<FieldResponseDto> getFieldsByFck(FormCompositeKey fck){
        Form form = getFormByFck(fck);
        List<Field> fields = form.getFields();
        List<FieldResponseDto> fieldResponseDtos = new ArrayList<>();
        for (Field field: fields){
            FieldResponseDto fieldResponseDto = convertToDto(field);
            fieldResponseDtos.add(fieldResponseDto);
        }
        return fieldResponseDtos;
    }
    private FieldResponseDto convertToDto(Field field){
        return FieldResponseDto.builder()
                .name(field.getName())
                .helpText(field.getHelpText())
                .isRequired(field.getIsRequired())
                .fieldType(field.getFieldType())
                .nextFieldsId(getNextFieldsIdFromMap(field.getOptions()))
                .regexId(field.getRegex() == null ? null : field.getRegex().getId())
                .formCompositeKey(field.getForm().getId())
                .build();
    }
    private Map<String, Long> getNextFieldsIdFromMap(Map<String, Field> nextFieldsMap){
        Map<String, Long> nextFieldsId = new HashMap<>();
        for(Map.Entry<String, Field> entry : nextFieldsMap.entrySet()){
            nextFieldsId.put(entry.getKey(), entry.getValue().getId());
        }
        return nextFieldsId;
    }

    public FormCompositeKey createAndGetFck(){
         Form form = createForm();
         return form.getId();
    }

    public FormCompositeKey duplicateAndGetFck(FormCompositeKey fck){
         Form form = duplicateForm(fck);
         return form.getId();
    }

    public FormResponseDto getLatestForm(Long id){
         Form form = formRepository.findLatestForm(id);
         List<AccountDto> authorizedAccountDtoList = accountService.getAccountDtoList(form.getAuthorizedAccounts());
         List<FieldResponseDto> fieldResponseDtoList = fieldService.getFieldResponseDtoList(form.getFields());
         FormResponseDto frDto = FormResponseDto.builder()
                 .name(form.getName())
                 .description(form.getDescription())
                 .isFinal(form.isFinal())
                 .authorizedAccounts(authorizedAccountDtoList)
                 .fields(fieldResponseDtoList)
                 .build();
         return frDto;
    }
}
