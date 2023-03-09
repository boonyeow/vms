package com.vms.controller;

import com.vms.dto.FormResponseDto;
import com.vms.dto.FormSectionDto;
import com.vms.dto.FormDto;
import com.vms.dto.WorkflowResponseDto;
import com.vms.model.Workflow;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.FormService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping
    public ResponseEntity<List<FormResponseDto>> getAllForms(){
        List<FormResponseDto> forms = formService.getAllFormDto();
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/{id}/{revisionId}")
    public ResponseEntity<FormResponseDto> getFormByID(@PathVariable Long id,
                                                       @PathVariable Integer revisionNo){
        FormCompositeKey fck = new FormCompositeKey(id, revisionNo);
        FormResponseDto form = formService.getFormDtoByFck(fck);
        return ResponseEntity.ok(form);
    }

    @PostMapping
    public ResponseEntity<Void> createForm(){
        formService.createForm();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/{revisionId}/duplicate")
    public ResponseEntity<Void> duplicateForm(@PathVariable Long id,
                                              @PathVariable Integer revisionId){
        formService.duplicateForm(new FormCompositeKey(id, revisionId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/{revisionId}")
    public ResponseEntity<Void> updateForm(@PathVariable Long id,
                                           @PathVariable Integer revisionId,
                                           @RequestBody FormDto request,
                                           @RequestParam boolean applyChanges){
        FormCompositeKey fck = new FormCompositeKey(id, revisionId);
        formService.updateForm(fck, request, applyChanges);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/{revisionId}/authorizedAccount")
    public ResponseEntity<Void> updateAuthorizedAccount(@PathVariable Long id,
                                                        @PathVariable Integer revisionId,
                                                        @RequestBody List<String> emails) {
        formService.updateFormAuthorizedAccounts(new FormCompositeKey(id, revisionId), emails);
        return ResponseEntity.ok().build();
    }
}
