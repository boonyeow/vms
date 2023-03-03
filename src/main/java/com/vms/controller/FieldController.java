package com.vms.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vms.model.Field;
import com.vms.service.FieldService;

@RestController
@RequestMapping("/fields")
public class FieldController {
    
    @Autowired
    private FieldService fieldService;
    
    @GetMapping("")
    public Iterable<Field> getAllFields() {
        return fieldService.getAllFields();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Field> getFieldById(@PathVariable Long id) {
        Optional<Field> field = fieldService.getFieldById(id);
        if (field.isPresent()) {
            return new ResponseEntity<>(field.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("")
    public ResponseEntity<Field> createField(@RequestBody Field field) {
        return new ResponseEntity<>(fieldService.createField(field), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Field> updateField(@PathVariable Long id, @RequestBody Field field) {
        Optional<Field> existingField = fieldService.getFieldById(id);
        if (existingField.isPresent()) {
            return new ResponseEntity<>(fieldService.updateField(id, field), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        Optional<Field> field = fieldService.getFieldById(id);
        if (field.isPresent()) {
            fieldService.deleteField(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
