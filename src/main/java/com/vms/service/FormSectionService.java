package com.vms.service;

import com.vms.dto.FormDto;
import com.vms.dto.FormSectionDto;
import com.vms.model.Account;
import com.vms.model.Form;
import com.vms.model.FormSection;
import com.vms.repository.FormSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FormSectionService {
    @Autowired
    private FormSectionRepository formSectionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FormService formService;

    public Iterable<FormSection> findAllByFormIdWithAccounts(Long formId){
        return formSectionRepository.findAllByFormIdWithAccounts(formId);
    }

    public void createFormSection(Long formId, FormSectionDto request) {
        Form form = formService.getFormById(formId);
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
            // See whether want check Forms authorizedAccounts
            // A simple if forms not authorized, you cant authorize them here in form_section
        }

        if(authorizedAccounts.isEmpty()){
            throw new RuntimeException("Form section must have at least one authorized user");
        }

        FormSection formSection = new FormSection();
        formSection.setForm(form);
        formSection.setAuthorizedAccounts(authorizedAccounts);
        formSectionRepository.save(formSection);
    }

    public void removeFormSection(Long formId, Long formSectionId){
        Form form = formService.getFormById(formId);
        FormSection formSection = formSectionRepository.findById(formSectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));
        // Implement check to see if form has started
        if(!form.isFinished()){
            formSectionRepository.delete(formSection);
        }
    }

    public void updateFormSection(Long formId, Long sectionId, FormSectionDto request){
        // might not even need to check form
        Form form = formService.getFormById(formId);
        FormSection formSection = formSectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));
        if(form.isFinished()){
            throw new RuntimeException("Form has been finalized, no updates allowed");
        }

        List<Account> authorizedAccounts = new ArrayList<>();
        List<Long> ids = request.getAuthorizedAccountIds();
        for(Long id : ids){
            Account account = accountService.getAccountById(id).orElseThrow();
            if (!authorizedAccounts.contains(account)) {
                authorizedAccounts.add(account);
            } else {
                throw new RuntimeException("Duplicated accounts provided");
            }
        }
        formSection.setAuthorizedAccounts(authorizedAccounts);
    }

    public void addAuthorizedAccount(Long formId, Long formSectionId, List<String> emails){
        Form form = formService.getFormById(formId);
        FormSection formSection = formSectionRepository.findById(formSectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));
        if(form.isFinished()){
            throw new RuntimeException("Form has been finalized, no updates allowed");
        };
        for(String email : emails){
            Account account = accountService.getAccountByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Account " + email + " not found"));
            if (!formSection.getAuthorizedAccounts().contains(account)) {
                formSection.getAuthorizedAccounts().add(account);
            } else {
                throw new RuntimeException("Duplicated accounts detected");
            }
        }
        formSectionRepository.save(formSection);
    }

    public void removeAuthorizedAccount(Long formId, Long formSectionId, List<String> emails){
        Form form = formService.getFormById(formId);
        FormSection formSection = formSectionRepository.findById(formSectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));

        if(form.isFinished()){
            throw new RuntimeException("Form has been finalized, no updates allowed");
        }

        List<Account> accountsToRemove = new ArrayList<>();
        for(String email: emails){
            Account account = accountService.getAccountByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Account " + email + " not found"));
            accountsToRemove.add(account);
        }
        formSection.getAuthorizedAccounts().removeAll(accountsToRemove);
        formSectionRepository.save(formSection);
    }
}
