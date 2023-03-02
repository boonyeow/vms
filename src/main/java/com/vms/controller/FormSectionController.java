package com.vms.controller;

import com.vms.model.Form;
import com.vms.model.FormSection;
import com.vms.service.FormSectionService;
import com.vms.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/form_section")
public class FormSectionController {
    private FormSectionService formSectionService;

    public FormSectionController(FormSectionService formSectionService) {
        this.formSectionService = formSectionService;
    }

    @GetMapping
    public ResponseEntity<Iterable<FormSection>> getAllFormSections() {
        return ResponseEntity.ok(formSectionService.getAllFormSections());
    }

    @GetMapping("/{id}")
    public FormSection getFormSectionById(@PathVariable Long id) {
        return formSectionService.getFormSectionById(id);
    }

    @PostMapping
    public FormSection createFormSection(@RequestBody FormSection formSection,
                                         @RequestParam Long accountId) {
        return formSectionService.createFormSection(formSection, accountId);
    }


}
