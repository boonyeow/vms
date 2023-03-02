package com.vms.service;

import com.vms.exception.EntityNotFoundException;
import com.vms.model.Account;
import com.vms.model.FormSection;
import com.vms.repository.AccountRepository;
import com.vms.repository.FormSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FormSectionService {
    @Autowired
    private FormSectionRepository formSectionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Iterable<FormSection> getAllFormSections() {
        return formSectionRepository.findAll();
    }

    public FormSection getFormSectionById(Long id) {
        return formSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FormSection with id " + id + " not found"));
    }

    public FormSection createFormSection(FormSection formSection, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + accountId + " not found"));
        formSection.getAuthorizedUsers().add(account);
        return formSectionRepository.save(formSection);
    }
}
