package com.vms.controller;

import com.vms.dto.FieldDto;
import com.vms.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms/{formId}/sections/{sectionId}/fields")
public class FieldController {
    @Autowired
    private FieldService fieldService;

    @PostMapping
    public ResponseEntity<FieldDto> createField(@PathVariable Long formId,
                                                @PathVariable Long sectionId,
                                                @RequestBody FieldDto request){
        fieldService.createField(formId, sectionId, request);
        return ResponseEntity.ok().build();
    }
}
