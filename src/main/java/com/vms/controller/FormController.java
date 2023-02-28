package com.vms.controller;

import com.vms.model.Form;
import com.vms.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/form")
public class FormController {

    private FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Form>> getAllForms() {
        return ResponseEntity.ok(formService.getAllForms());
    }

    @GetMapping("/searchByFormName")
    public ResponseEntity<Form> getFormByFormName(@RequestParam(value = "formName") String formName) {
        Form form = formService.getFormByFormName(formName);
        if (form == null) {
            throw new RuntimeException("form not found");
        }
        return ResponseEntity.ok(form);
    }

    @PostMapping
    public ResponseEntity<Form> createForm(@RequestBody Form form) {
        boolean isCreated = formService.createForm(form);
        if(!isCreated){
            throw new RuntimeException("form not created");
        }
        return ResponseEntity.ok(form);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateForm(@PathVariable Long id, @RequestBody Form form) {
        boolean isUpdated = formService.updateForm(id, form);
        if(!isUpdated){
            throw new RuntimeException("form not updated");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id){
        boolean isDeleted = formService.deleteAccount(id);
        if(!isDeleted){
            throw new RuntimeException("form not deleted");
        }
        return ResponseEntity.noContent().build();
    }



}
