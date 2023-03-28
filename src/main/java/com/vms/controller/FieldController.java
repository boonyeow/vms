package com.vms.controller;

import com.vms.dto.FieldRequestDto;
import com.vms.dto.FieldResponseDto;
import com.vms.model.Field;
import com.vms.model.Form;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.FieldService;
import com.vms.service.FormService;
import com.vms.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/fields")
public class FieldController {
    @Autowired
    private FieldService fieldService;
    @Autowired
    private FormService formService;
    @Autowired
    private FormRepository formRepository;


    @PostMapping("/create")
    public ResponseEntity<Void> createField(@RequestBody FieldRequestDto request, @RequestParam String formId, @RequestParam String revisionNo) {
        FormCompositeKey fck = new FormCompositeKey(Long.parseLong(formId), Integer.parseInt(revisionNo));
        try {
            Form form = formService.getFormByFck(fck);
            fieldService.createField(request, form);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dto")
    public ResponseEntity<List<FieldResponseDto>> getAllFieldsDto() {
        List<FieldResponseDto> fieldResponseDtos = fieldService.getAllFieldsDto();
        return ResponseEntity.ok(fieldResponseDtos);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<FieldResponseDto> getFieldDtoById(@PathVariable Long id) {
        FieldResponseDto fieldResponseDto = fieldService.getFieldDtoById(id);
        return ResponseEntity.ok(fieldResponseDto);
    }

}
