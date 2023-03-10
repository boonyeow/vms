package com.vms.controller;

import com.vms.dto.FormResponseDto;
import com.vms.dto.FormDto;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}/{revisionNo}")
    public ResponseEntity<FormResponseDto> getFormByID(@PathVariable Long id,
                                                       @PathVariable int revisionNo){
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
                                              @PathVariable int revisionNo){
        formService.duplicateForm(new FormCompositeKey(id, revisionNo));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/{revisionId}")
    public ResponseEntity<Void> updateForm(@PathVariable Long id,
                                           @PathVariable int revisionNo,
                                           @RequestBody FormDto request,
                                           @RequestParam boolean applyChanges){
        FormCompositeKey fck = new FormCompositeKey(id, revisionNo);
        formService.updateForm(fck, request, applyChanges);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/{revisionId}/authorizedAccount")
    public ResponseEntity<Void> updateAuthorizedAccount(@PathVariable Long id,
                                                        @PathVariable int revisionNo,
                                                        @RequestBody List<String> emails) {
        formService.updateFormAuthorizedAccounts(new FormCompositeKey(id, revisionNo), emails);
        return ResponseEntity.ok().build();
    }
}
