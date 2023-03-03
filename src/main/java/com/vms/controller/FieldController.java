package com.vms.controller;

import com.vms.dto.FieldDto;
import com.vms.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms/{formId}/sections/{formSectionId}/fields")
public class FieldController {
    @Autowired
    private FieldService fieldService;
    @PostMapping
    public ResponseEntity<Void> createField(@PathVariable Long formId,
                                            @PathVariable Long formSectionId,
                                            @RequestBody FieldDto request ){
        fieldService.createField(formId, formSectionId, request);
        return ResponseEntity.ok().build();
    }
}
