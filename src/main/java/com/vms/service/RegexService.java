package com.vms.service;

import com.vms.dto.RegexDto;
import com.vms.model.Regex;
import com.vms.repository.RegexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class RegexService {
    @Autowired
    private RegexRepository regexRepository;

    public void createRegex(RegexDto request){
        if(regexRepository.existsByPattern(request.getPattern())){
            throw new RuntimeException("Existing pattern found");
        }

        Regex regex = Regex.builder()
                .name(request.getName())
                .pattern(request.getPattern())
                .build();
        regexRepository.save(regex);
    }

    public void deleteRegex(Long id){
        Regex regex = getRegexById(id);
        if(regex.getFields().isEmpty()){
            // Reject delete if it violates referential integrity
            throw new RuntimeException("Not allowed. Please ensure no other fields are using the regex before deleting.");
        }

        regexRepository.delete(regex);
    }

    public void updateRegex(Long id, RegexDto request){
        Regex regex = getRegexById(id);
        if(regexRepository.existsByPattern(request.getPattern())){
            throw new RuntimeException("Existing pattern found");
        }

        if(regex.getFields().isEmpty()){
            // Check for referential integrity violation
           throw new RuntimeException("Not allowed. Please ensure no other fields are using the regex before deleting.");
        }

        regex.setName(request.getName());
        regex.setPattern(request.getPattern());
        regexRepository.save(regex);
    }

    public Iterable<Regex> getAllRegex(){
        return regexRepository.findAll();
    }

    public Regex getRegexById(Long id){
        return regexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Regex not found"));
    }

    public Boolean isPatternMatched(String input, String pattern){
        return Pattern.matches(pattern, input);
    }
}
