package com.vms.service;

import com.vms.dto.FormDto;
import com.vms.dto.FormResponseDto;
import com.vms.dto.FormSectionDto;
import com.vms.dto.FormSectionResponseDto;
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

    public FormResponseDto getFormDtoById(Long formId){
        Form form = getFormById(formId);

        List<FormSection> formSections = form.getFormSections();
        return FormResponseDto.builder()
                .name(form.getName())
                .description(form.getDescription())
                .formSections(getFormSectionDtoList(form.getFormSections()))
                .authorizedAccounts(accountService.getAccountDtoList(form.getAuthorizedAccounts()))
                .build();
    }

    public Form getFormById(Long formId){
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found")
                );
        return form;
    }

    private List<FormSectionResponseDto> getFormSectionDtoList(List<FormSection> formSections){
        List<FormSectionResponseDto> fsDtoList = new ArrayList<>();
        for(FormSection fs: formSections){
            fsDtoList.add(FormSectionResponseDto.builder()
                    .id(fs.getId())
                    .authorizedAccounts(accountService.getAccountDtoList(fs.getAuthorizedAccounts()))
                    .build()
            );
        }
        return fsDtoList;
    }
}
