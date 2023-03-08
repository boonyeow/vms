package com.vms.controller;

import com.vms.dto.FieldResponseDto;
import com.vms.model.FieldResponse;
import com.vms.service.FieldResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/field-responses")
public class FieldResponseController {
    private final FieldResponseService fieldResponseService;

    @Autowired
    public FieldResponseController(FieldResponseService fieldResponseService) {
        this.fieldResponseService = fieldResponseService;
    }

    @GetMapping
    public ResponseEntity<List<FieldResponse>> getAllFieldResponses() {
        List<FieldResponse> fieldResponses = fieldResponseService.getAllFieldResponses();
        return new ResponseEntity<>(fieldResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldResponse> getFieldResponseById(@PathVariable("id") Long id) {
        FieldResponse fieldResponse = fieldResponseService.getFieldResponseById(id);
        return new ResponseEntity<>(fieldResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FieldResponse> createFieldResponse(FieldResponseDto fieldResponseDto) {
        FieldResponse fieldResponse = fieldResponseService.createFieldResponse(fieldResponseDto);
        return new ResponseEntity<>(fieldResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldResponse> updateFieldResponse(@PathVariable("id") Long id, FieldResponseDto fieldResponseDto) {
        FieldResponse fieldResponse = fieldResponseService.updateFieldResponse(id, fieldResponseDto);
        return new ResponseEntity<>(fieldResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFieldResponse(@PathVariable("id") Long id) {
        fieldResponseService.deleteFieldResponse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
