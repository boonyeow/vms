package com.vms.service;

import com.vms.dto.FormDto;
import com.vms.dto.FormSectionDto;
import com.vms.exception.EntityNotFoundException;
import com.vms.model.Account;
import com.vms.model.FormSection;
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
    private final FormRepository formRepository;

    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public Iterable<Form> getAllForms() {
        return formRepository.findAll();
    }

    public boolean createForm(FormDto request){
        if(formRepository.findByName(request.getName()).isEmpty()){
            // creating for the first time
            Form form = Form.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .isFinished(request.isFinished())
                    .build();
            formRepository.save(form);
            return true;
        }
        return false;
    }

    public boolean updateForm(Long id, FormDto request){
        Form form = formRepository.findById(id).orElseThrow(() -> new RuntimeException("Form not found"));
        if(!form.isFinished()){
            form.setName(request.getName());
            form.setDescription(request.getDescription());
            form.setFinished(request.isFinished());
            formRepository.save(form);
            return true;
        }
        return false;
    }

    public boolean deleteAccount(Long id){
        Optional<Form> optionalForm = formRepository.findById(id);
        if (optionalForm.isPresent()){
            formRepository.delete(optionalForm.get());
            return true;
        }
        return false;
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
