package com.vms.controller;


import com.vms.dto.RegexDto;
import com.vms.model.Regex;
import com.vms.service.RegexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/regex")
public class RegexController {
    @Autowired
    private RegexService regexService;

    @PostMapping
    public ResponseEntity<Void> createRegex(@RequestBody RegexDto request){
        regexService.createRegex(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RegexDto>> getAllRegex(){
        return ResponseEntity.ok(regexService.getAllRegexDto());
    }
}
