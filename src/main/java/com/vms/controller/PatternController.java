package com.vms.controller;
import com.vms.model.Pattern;

import com.vms.service.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patterns")
public class PatternController {
    @Autowired
    private PatternService patternService;

    @GetMapping
    public ResponseEntity<Iterable<Pattern>> getAllPatterns(){
        return ResponseEntity.ok(patternService.getAllPatterns());
    }

    @PostMapping
    public ResponseEntity<Pattern> createPattern(@RequestBody Pattern pattern) {
        boolean isCreated = patternService.createPattern(pattern);
        return ResponseEntity.ok(pattern);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable Long id){
        boolean isDeleted = patternService.deletePattern(id);
        return ResponseEntity.noContent().build();
    }
}
