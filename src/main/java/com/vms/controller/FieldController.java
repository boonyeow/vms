package com.vms.controller;

import com.vms.dto.FieldDto;
import com.vms.model.Field;
import com.vms.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("api/fields")
public class FieldController {
    @Autowired
    private FieldService fieldService;

    @PostMapping
    public ResponseEntity<Void> createField(@RequestBody FieldDto request) {
        fieldService.createField(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dto")
    public ResponseEntity<List<FieldDto>> getAllFieldsDto() {
        List<FieldDto> fields = fieldService.getAllFieldsDto();
        return ResponseEntity.ok(fields);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<FieldDto> getFieldDtoById(@PathVariable Long id) {
        FieldDto field = fieldService.getFieldDtoById(id);
        return ResponseEntity.ok(field);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Field> getFieldById(@PathVariable Long id) {
        Field field = fieldService.getFieldById(id);
        return ResponseEntity.ok(field);
    }
}
