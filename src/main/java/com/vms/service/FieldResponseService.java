package com.vms.service;

import com.vms.dto.FieldResponseDto;
import com.vms.model.FieldResponse;
import com.vms.repository.FieldResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FieldResponseService {
        private final FieldResponseRepository fieldResponseRepository;

    @Autowired
    public FieldResponseService(FieldResponseRepository fieldResponseRepository) {
        this.fieldResponseRepository = fieldResponseRepository;
    }

    public List<FieldResponse> getAllFieldResponses() {
        return fieldResponseRepository.findAll();
    }

    public FieldResponse getFieldResponseById(Long id) {
        return fieldResponseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    public FieldResponse createFieldResponse(FieldResponseDto fieldResponseDto) {
        FieldResponse fieldResponse = FieldResponse.builder()
                .responseText(fieldResponseDto.getResponseText())
                .fieldType(fieldResponseDto.getFieldType())
                .build();

        return fieldResponseRepository.save(fieldResponse);
    }

    public FieldResponse updateFieldResponse(Long id, FieldResponseDto fieldResponseDto) {
        FieldResponse fieldResponse = getFieldResponseById(id);

        fieldResponse = fieldResponse.toBuilder()
                .responseText(fieldResponseDto.getResponseText())
                .fieldType(fieldResponseDto.getFieldType())
                .build();

        return fieldResponseRepository.save(fieldResponse);
    }

    public void deleteFieldResponse(Long id) {
        FieldResponse fieldResponse = getFieldResponseById(id);
        fieldResponseRepository.delete(fieldResponse);
    }
}
