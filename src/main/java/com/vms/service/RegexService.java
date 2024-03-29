package com.vms.service;

import com.vms.dto.RegexDto;
import com.vms.exception.DuplicatePatternsException;
import com.vms.exception.ReferentialIntegrityException;
import com.vms.exception.RegexNotFoundException;
import com.vms.model.Regex;
import com.vms.repository.RegexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class RegexService {
    @Autowired
    private RegexRepository regexRepository;

    public void createRegex(RegexDto request){
        if(regexRepository.existsByPattern(request.getPattern())){
            throw new DuplicatePatternsException("Existing pattern found");
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
            throw new ReferentialIntegrityException("Not allowed. Please ensure no other fields are using the regex before deleting.");
        }

        regexRepository.delete(regex);
    }

    public void updateRegex(Long id, RegexDto request){
        Regex regex = getRegexById(id);
        if(regexRepository.existsByPattern(request.getPattern())){
            throw new DuplicatePatternsException("Existing pattern found");
        }

        if(regex.getFields().isEmpty()){
            // Check for referential integrity violation
           throw new ReferentialIntegrityException("Not allowed. Please ensure no other fields are using the regex before deleting.");
        }

        regex.setName(request.getName());
        regex.setPattern(request.getPattern());
        regexRepository.save(regex);
    }

    public Iterable<Regex> getAllRegex(){
        return regexRepository.findAll();
    }

    public List<RegexDto> getAllRegexDto() {
        Iterable<Regex> regexes = getAllRegex();
        List<RegexDto> regexDtoList = new ArrayList<>();
        for(Regex regex : regexes){
            regexDtoList.add(RegexDto.builder()
                    .id(regex.getId())
                    .name(regex.getName())
                    .pattern(regex.getPattern())
                    .build());
        }
        return regexDtoList;
    }
    public Regex getRegexById(Long id){
        return regexRepository.findById(id)
                .orElseThrow(() -> new RegexNotFoundException("Regex not found"));
    }

    public Boolean isPatternMatched(String input, String pattern){
        return Pattern.matches(pattern, input);
    }
}
