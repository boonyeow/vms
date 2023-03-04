package com.vms.controller;

import com.vms.dto.FormSectionDto;
import com.vms.model.FormSection;
import com.vms.service.FormSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms/{formId}/sections")
public class FormSectionController {
    @Autowired
    private FormSectionService formSectionService;

    @GetMapping
    public ResponseEntity<Iterable<FormSectionDto>> findAllByFormIdWithAccounts(@PathVariable Long formId) {
        return ResponseEntity.ok(formSectionService.getAllFormSectionsDtoByFormId(formId));
    }

    @PostMapping
    public ResponseEntity<Void> createFormSection(@PathVariable Long formId,
            @RequestBody FormSectionDto request) {
        formSectionService.createFormSection(formId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<Void> updateFormSection(@PathVariable Long formId,
            @PathVariable Long sectionId,
            @RequestBody FormSectionDto request) {
        formSectionService.updateFormSection(formId, sectionId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> removeFormSection(@PathVariable Long formId,
            @PathVariable Long sectionId) {
        formSectionService.removeFormSection(formId, sectionId);
        return ResponseEntity.ok().build();
    }
}