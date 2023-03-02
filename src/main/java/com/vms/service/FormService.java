package com.vms.service;

import com.vms.dto.FormDto;
import com.vms.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.vms.model.Form;
import com.vms.repository.FormRepository;

import java.util.Optional;

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
        Optional<Form> optionalForm = formRepository.findById(id);
        if (optionalForm.isPresent()){
            Form existingForm = optionalForm.get();
            existingForm.setName(request.getName());
            existingForm.setDescription(request.getDescription());
            existingForm.setFinished(request.isFinished());
            formRepository.save(existingForm);
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
}
