package com.vms.controller;

import com.vms.dto.FieldResponseDto;
import com.vms.dto.FormResponseDto;
import com.vms.dto.FormDto;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping
    public ResponseEntity<List<FormResponseDto>> getAllForms(){
        List<FormResponseDto> forms = formService.getAllFormDto();
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/state")
    public ResponseEntity<List<FormResponseDto>> getAllFormsByState(@RequestParam Boolean state){
        List<FormResponseDto> forms = formService.getFormDtoByState(state);
        return ResponseEntity.ok(forms);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FormResponseDto> getLatestForm(@PathVariable Long id){
        return ResponseEntity.ok(formService.getLatestForm(id));
    }

    @GetMapping("/{id}/{revisionNo}")
    public ResponseEntity<FormResponseDto> getFormByID(@PathVariable Long id,
                                                       @PathVariable int revisionNo){
        FormCompositeKey fck = new FormCompositeKey(id, revisionNo);
        FormResponseDto form = formService.getFormDtoByFck(fck);
        return ResponseEntity.ok(form);
    }

    @PostMapping
    public ResponseEntity<FormCompositeKey> createForm(){
        FormCompositeKey fck = formService.createAndGetFck();
        return ResponseEntity.ok(fck);
    }

    @PostMapping("/{id}/{revisionNo}/duplicate")
    public ResponseEntity<FormCompositeKey> duplicateForm(@PathVariable Long id,
                                              @PathVariable int revisionNo){
        FormCompositeKey fck = formService.duplicateAndGetFck(new FormCompositeKey(id, revisionNo));
        return ResponseEntity.ok(fck);
    }

    @PutMapping("/{id}/{revisionNo}")
    public ResponseEntity<Void> updateForm(@PathVariable Long id,
                                           @PathVariable int revisionNo,
                                           @RequestBody FormDto request,
                                           @RequestParam boolean applyChanges){
        FormCompositeKey fck = new FormCompositeKey(id, revisionNo);
        formService.updateForm(fck, request, applyChanges);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/{revisionNo}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id,
                                           @PathVariable int revisionNo){
        FormCompositeKey fck = new FormCompositeKey(id, revisionNo);
        formService.deleteForm(fck);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/{revisionNo}/authorizedAccount")
    public ResponseEntity<Void> updateAuthorizedAccount(@PathVariable Long id,
                                                        @PathVariable int revisionNo,
                                                        @RequestBody List<String> emails) {
        formService.updateFormAuthorizedAccounts(new FormCompositeKey(id, revisionNo), emails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/{revisionNo}/fields")
    public ResponseEntity<List<FieldResponseDto>> getFormFields(@PathVariable Long id,
                                                        @PathVariable int revisionNo){
        FormCompositeKey fck = new FormCompositeKey(id, revisionNo);
        List<FieldResponseDto> fieldsResponseDtos = formService.getFieldsByFck(fck);
        return ResponseEntity.ok(fieldsResponseDtos);
    }
}
