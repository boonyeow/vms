package com.vms.service;

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

    public Form getFormByFormName(String formName){
        return formRepository.findByFormName(formName);
    }

    public boolean createForm(Form form){
        if(getFormByFormName(form.getFormName()) == null){
            // creating for the first time
            formRepository.save(form);
            return true;
        }
        return false;
    }

    public boolean updateForm(Long id, Form form){
        Optional<Form> optionalForm = formRepository.findById(id);
        if (optionalForm.isPresent()){
            Form existingForm = optionalForm.get();
            existingForm.setFormName(form.getFormName());
            existingForm.setDescription(form.getDescription());
            existingForm.setFinished(form.getIsFinished());
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
