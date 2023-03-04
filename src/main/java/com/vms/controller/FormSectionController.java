package com.vms.controller;

import com.vms.dto.FormSectionDto;
import com.vms.model.FormSection;
import com.vms.service.FormSectionService;
import com.vms.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/forms/{formId}/sections")
public class FormSectionController {
    @Autowired
    private FormSectionService formSectionService;

    @GetMapping
    public ResponseEntity<Iterable<FormSectionDto>> findAllByFormIdWithAccounts(@PathVariable Long formId){
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

    @PostMapping("/{sectionId}/authorized_accounts")
    public ResponseEntity<Void> addAuthorizedAccount(@PathVariable Long formId,
                                                  @PathVariable Long sectionId,
                                                  @RequestBody List<String> emails) {
        formSectionService.addAuthorizedAccount(formId, sectionId, emails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sectionId}/authorized_accounts")
    public ResponseEntity<Void> removeAuthorizedAccount(@PathVariable Long formId,
                                                      @PathVariable Long sectionId,
                                                      @RequestBody List<String> emails) {
        formSectionService.removeAuthorizedAccount(formId, sectionId, emails);
        return ResponseEntity.ok().build();
    }
}