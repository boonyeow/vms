package com.vms.service;
import com.vms.model.FieldResponse;
import com.vms.repository.FieldResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FieldResponseService {

    @Autowired
    private FieldResponseRepository fieldResponseRepository;

    public FieldResponse getFieldResponseById(Long id) {
        Optional<FieldResponse> optionalFieldResponse = fieldResponseRepository.findById(id);
        return optionalFieldResponse.orElse(null);
    }

    public FieldResponse createFieldResponse(FieldResponse fieldResponse) {
        return fieldResponseRepository.save(fieldResponse);
    }

    public FieldResponse updateFieldResponse(FieldResponse fieldResponse) {
        Optional<FieldResponse> optionalFieldResponse = fieldResponseRepository.findById(fieldResponse.getId());
        if (optionalFieldResponse.isPresent()) {
            return fieldResponseRepository.save(fieldResponse);
        } else {
            return null;
        }
    }

    public boolean deleteFieldResponse(Long id) {
        Optional<FieldResponse> optionalFieldResponse = fieldResponseRepository.findById(id);
        if (optionalFieldResponse.isPresent()) {
            fieldResponseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
