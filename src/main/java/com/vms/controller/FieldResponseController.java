package com.vms.controller;

import com.vms.model.FieldResponse;
import com.vms.service.FieldResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/field_responses")
public class FieldResponseController {
    @Autowired
    private FieldResponseService fieldResponseService;

    @GetMapping("/{id}")
    public ResponseEntity<FieldResponse> getFieldResponseById(@PathVariable Long id) {
        FieldResponse fieldResponse = fieldResponseService.getFieldResponseById(id);
        if (fieldResponse == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(fieldResponse);
        }
    }

    @PostMapping("/")
    public ResponseEntity<FieldResponse> createFieldResponse(@RequestBody FieldResponse fieldResponse) {
        fieldResponse = fieldResponseService.createFieldResponse(fieldResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldResponse> updateFieldResponse(@PathVariable Long id, @RequestBody FieldResponse fieldResponse) {
        fieldResponse.setId(id);
        fieldResponse = fieldResponseService.updateFieldResponse(fieldResponse);
        if (fieldResponse == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(fieldResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFieldResponse(@PathVariable Long id) {
        boolean deleted = fieldResponseService.deleteFieldResponse(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

