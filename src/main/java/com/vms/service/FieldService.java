package com.vms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vms.model.Field;
import com.vms.repository.FieldRepository;

@Service
public class FieldService {
    
    @Autowired
    private FieldRepository fieldRepository;
    
    public Iterable<Field> getAllFields() {
        return fieldRepository.findAll();
    }
    
    public Optional<Field> getFieldById(Long id) {
        return fieldRepository.findById(id);
    }
    
    public Field createField(Field field) {
        return fieldRepository.save(field);
    }
    
    public Field updateField(Long id, Field field) {
        field.setId(id);
        return fieldRepository.save(field);
    }
    
    public void deleteField(Long id) {
        fieldRepository.deleteById(id);
    }
}
