package com.vms.controller;

import com.vms.dto.FormDto;
import com.vms.dto.FormResponseDto;
import com.vms.model.Form;
import com.vms.model.FormSection;
import com.vms.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    private FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Form>> getAllForms() {
        return ResponseEntity.ok(formService.getAllForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormResponseDto> getFormById(@PathVariable Long id){
        return  ResponseEntity.ok(formService.getFormDtoById(id));
    }

    @PostMapping
    public ResponseEntity<FormDto> createForm(@RequestBody FormDto request) {
        boolean isCreated = formService.createForm(request);
        if(!isCreated){
            throw new RuntimeException("form not created");
        }
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateForm(@PathVariable Long id, @RequestBody FormDto formDto) {
        boolean isUpdated = formService.updateForm(id, formDto);
        if(!isUpdated){
            throw new RuntimeException("form not updated");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id){
        boolean isDeleted = formService.deleteForm(id);
        if(!isDeleted){
            throw new RuntimeException("form not deleted");
        }
        return ResponseEntity.noContent().build();
    }
}
