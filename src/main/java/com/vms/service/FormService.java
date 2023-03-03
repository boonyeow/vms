package com.vms.service;

import com.vms.dto.FormDto;
import com.vms.dto.FormSectionDto;
import com.vms.exception.EntityNotFoundException;
import com.vms.model.Account;
import com.vms.model.FormSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.vms.model.Form;
import com.vms.repository.FormRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormService {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private AccountService accountService;

    public Iterable<Form> getAllForms() {
        return formRepository.findAll();
    }

    public boolean createForm(FormDto request){
        if(formRepository.findByName(request.getName()).isEmpty()){
            // creating for the first time
            List<Long> authorizedAccountIds = request.getAuthorizedAccountIds();
            List<Account> authorizedAccounts = new ArrayList<>();
            for(Long id: authorizedAccountIds){
                Account account = accountService.getAccountById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
                if (!authorizedAccounts.contains(account)) {
                    authorizedAccounts.add(account);
                } else {
                    throw new RuntimeException("Duplicated accounts provided");
                }
            }
            Form form = Form.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .isFinished(request.isFinished())
                    .authorizedAccounts(authorizedAccounts)
                    .build();
            formRepository.save(form);
            return true;
        }
        return false;
    }

    public boolean updateForm(Long id, FormDto request){
        Form form = formRepository.findById(id).orElseThrow(() -> new RuntimeException("Form not found"));
        if(!form.isFinished()){
            List<Long> authorizedAccountIds = request.getAuthorizedAccountIds();
            List<Account> authorizedAccounts = new ArrayList<>();
            for(Long accountId: authorizedAccountIds){
                Account account = accountService.getAccountById(accountId)
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
                if (!authorizedAccounts.contains(account)) {
                    authorizedAccounts.add(account);
                } else {
                    throw new RuntimeException("Duplicated accounts provided");
                }
            }

            form.setName(request.getName());
            form.setDescription(request.getDescription());
            form.setFinished(request.isFinished());
            form.setAuthorizedAccounts(authorizedAccounts);
            formRepository.save(form);
            return true;
        }
        return false;
    }

    public boolean deleteForm(Long id){
        Optional<Form> optionalForm = formRepository.findById(id);
        if (optionalForm.isPresent()){
            formRepository.delete(optionalForm.get());
            return true;
        }
        return false;
    }

    public void addAuthorizedAccount(Long formId, List<String> emails){
        Form form = formRepository.findById(formId).orElseThrow(() -> new RuntimeException("Form not found"));
        if(form.isFinished()){
            throw new RuntimeException("Form has been finalized, no updates allowed");
        };
        for(String email : emails){
            Account account = accountService.getAccountByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Account " + email + " not found"));
            if (!form.getAuthorizedAccounts().contains(account)) {
                form.getAuthorizedAccounts().add(account);
            } else {
                throw new RuntimeException("Duplicated accounts detected");
            }
        }
        formRepository.save(form);
    }
    public void removeAuthorizedAccount(Long formId, List<String> emails){
        Form form = formRepository.findById(formId).orElseThrow(() -> new RuntimeException("Form not found"));
        if(form.isFinished()){
            throw new RuntimeException("Form has been finalized, no updates allowed");
        }

        List<Account> accountsToRemove = new ArrayList<>();
        for(String email: emails){
            Account account = accountService.getAccountByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Account " + email + " not found"));
            accountsToRemove.add(account);
        }
        form.getAuthorizedAccounts().removeAll(accountsToRemove);
        formRepository.save(form);
    }

    public FormDto getFormDtoById(Long formId){
        Form form = getFormById(formId);

        List<FormSection> formSections = form.getFormSections();
        List<FormSectionDto> fsDtoList = new ArrayList<>();

        for(FormSection fs : formSections){
            FormSectionDto fsDto = FormSectionDto.builder()
                    .id(fs.getId())
                    .authorizedAccountIds(fs.getAuthorizedAccounts().stream().map(Account::getId).collect(Collectors.toList()))
                    .build();
            fsDtoList.add(fsDto);
        }
        return FormDto.builder()
                .name(form.getName())
                .description(form.getDescription())
                .formSections(fsDtoList)
                .authorizedAccountIds(form.getAuthorizedAccounts().stream().map(Account::getId).collect(Collectors.toList()))
                .build();
    }

    public Form getFormById(Long formId){
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found")
                );
        return form;
    }
}
