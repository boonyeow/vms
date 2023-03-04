package com.vms.service;

import com.vms.dto.AccountDto;
import com.vms.dto.FormSectionDto;
import com.vms.dto.FormSectionResponseDto;
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
import java.util.stream.Collectors;

@Service
public class FormSectionService {
    @Autowired
    private FormSectionRepository formSectionRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private FormService formService;

    public Iterable<FormSectionDto> getAllFormSectionsDtoByFormId(Long formId) {
        List<FormSection> formSection = formSectionRepository.findByFormId(formId);
        List<FormSectionDto> fsDtoList = new ArrayList<>();
        for (FormSection fs : formSection) {
            FormSectionDto fsDto = FormSectionDto.builder()
                    .id(fs.getId())
                    .authorizedAccountIds(
                            fs.getAuthorizedAccounts().stream().map(Account::getId).collect(Collectors.toList()))
                    .build();
            fsDtoList.add(fsDto);
        }
        return fsDtoList;
    }

    public void createFormSection(Long formId, FormSectionDto request) {
        Form form = formService.getFormById(formId);

        List<Long> authorizedAccountIds = request.getAuthorizedAccountIds();
        List<Account> authorizedAccounts = new ArrayList<>();
        for (Long id : authorizedAccountIds) {
            Account account = accountService.getAccountById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            if (!authorizedAccounts.contains(account)) {
                authorizedAccounts.add(account);
            } else {
                throw new RuntimeException("Duplicated accounts provided");
            }
            // See whether want check Forms authorizedAccounts
            // A simple if forms not authorized, you cant authorize them here in
            // form_section
        }

        if (authorizedAccounts.isEmpty()) {
            throw new RuntimeException("Form section must have at least one authorized user");
        }

        FormSection formSection = new FormSection();
        formSection.setForm(form);
        formSection.setAuthorizedAccounts(authorizedAccounts);
        formSectionRepository.save(formSection);
    }

    public void removeFormSection(Long formId, Long formSectionId) {
        Form form = formService.getFormById(formId);
        FormSection formSection = formSectionRepository.findById(formSectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));
        // Implement check to see if form has started
        if (!form.isFinished()) {
            formSectionRepository.delete(formSection);
        }
    }

    public void updateFormSection(Long formId, Long sectionId, FormSectionDto request) {
        // might not even need to check form
        Form form = formService.getFormById(formId);
        FormSection formSection = formSectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));
        if (form != formSection.getForm()) {
            throw new RuntimeException("Form ID provided does not match the Form section's associated Form");
        }
        if (form.isFinished()) {
            throw new RuntimeException("Form has been finalized, no updates allowed");
        }

        List<Account> authorizedAccounts = new ArrayList<>();
        List<Long> authorizedAccountIds = request.getAuthorizedAccountIds();
        for (Long id : authorizedAccountIds) {
            Account account = accountService.getAccountById(id).orElseThrow();
            if (!authorizedAccounts.contains(account)) {
                authorizedAccounts.add(account);
            } else {
                throw new RuntimeException("Duplicated accounts provided");
            }
        }
        formSection.setAuthorizedAccounts(authorizedAccounts);
        formSectionRepository.save(formSection);
    }

    public FormSectionResponseDto getFormSectionDtoById(Long formSectionId) {
        FormSection formSection = getFormSectionById(formSectionId);

        return FormSectionResponseDto.builder()
                .authorizedAccounts(accountService.getAccountDtoList(formSection.getAuthorizedAccounts()))
                .build();
    }

    public FormSection getFormSectionById(Long formSectionId) {
        FormSection formSection = formSectionRepository.findById(formSectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Form section not found"));
        return formSection;
    }

    public void saveFormSection(FormSection formSection){
        formSectionRepository.save(formSection);
    }
}
