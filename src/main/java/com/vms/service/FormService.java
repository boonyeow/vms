package com.vms.service;

import com.vms.dto.AccountDto;
import com.vms.dto.FormDto;
import com.vms.dto.FormResponseDto;
import com.vms.dto.WorkflowDto;
import com.vms.model.Account;
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

     public void createForm(){
         FormCompositeKey fck = new FormCompositeKey();
         fck.setRevisionNo(0);
         Form form = Form.builder()
                 .id(fck)
                 .name("Untitled Form")
                 .description("")
                 .isFinal(false)
                 .authorizedAccounts(new ArrayList<>())
                 .workflows(new HashSet<>())
                 .build();
         formRepository.save(form);
     }

    public void duplicateForm(FormCompositeKey fck){
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
                 .build();
         formRepository.save(duplicatedForm);
    }

    public void updateForm(FormCompositeKey fck, FormDto request, boolean applyChanges){
         Form form = getFormByFck(fck);
         form.setName(request.getName());
         form.setDescription(request.getDescription());
         form.setFinal(request.isFinal());

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
         formRepository.save(form);
     }

     // Remember to test it to see if deleting form affects FormSubmission and Workflow
    public void deleteForm(FormCompositeKey fck){
        Form form = getFormByFck(fck);
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
            FormResponseDto formResponseDto = FormResponseDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .authorizedAccounts(authorizedAccounts)
                    .build();
             formResponses.add(formResponseDto);
         }
         return formResponses;
     }

    public FormResponseDto getFormDtoByFck(FormCompositeKey fck){
        Form form = getFormByFck(fck);
        return FormResponseDto.builder()
                .id(form.getId())
                .name(form.getName())
                .description(form.getDescription())
                .isFinal(form.isFinal())
                .authorizedAccounts(accountService.getAccountDtoList(form.getAuthorizedAccounts()))
                .build();
    }

    public Form getFormByFck(FormCompositeKey fck){
        return formRepository.findById(fck)
                .orElseThrow(() -> new RuntimeException("Form not found")
                );
    }
}
