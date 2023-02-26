package com.vms.service;

import com.vms.model.Account;
import com.vms.model.Pattern;
import com.vms.repository.AccountRepository;
import com.vms.repository.PatternRepository;
import org.springframework.stereotype.Service;

@Service
public class PatternService {
    private final PatternRepository patternRepository;

    public PatternService(PatternRepository patternRepository) {
        this.patternRepository = patternRepository;
    }

    public Iterable<Pattern> getAllPatterns() { return patternRepository.findAll();}

    public boolean createPattern(Pattern pattern){
        patternRepository.save(pattern);
        return true;
    }

    public boolean deletePattern(Long id){
        patternRepository.deleteById(id);
        return true;
    }
}
