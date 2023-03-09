package com.vms.service;

import com.vms.dto.FieldDto;
import com.vms.model.Field;
import com.vms.model.enums.FieldType;
import com.vms.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FieldService {
    @Autowired
    private FieldRepository fieldRepository;

    public void createField(FieldDto request){
        Map<String, Field> nextFields = getNextFieldsFromDto(request.getNextFieldsId());
        Field field = Field.builder()
                .name(request.getName())
                .label(request.getLabel())
                .helpText(request.getHelpText())
                .isRequired(request.isRequired())
                .fieldType(request.getFieldType())
                .options(request.getOptions())
                .nextFields(nextFields)
                .build();
        fieldRepository.save(field);
    }

    private Map<String, Field> getNextFieldsFromDto(Map<String, Long> nextFieldsId) {
        Map<String, Field> nextFieldsMap = new HashMap<>();
        if (nextFieldsId != null){
            for(String option : nextFieldsId.keySet()){
                Long linkedFieldId = nextFieldsId.get(option);
                Field linkedField = fieldRepository.findById(linkedFieldId).orElseThrow(() -> new RuntimeException("Field not found"));
                if(linkedField.getFieldType() != FieldType.TEXTBOX){
                    throw new RuntimeException("Recursive structure found");
                }
                nextFieldsMap.put(option, linkedField);
            }
        }
        return nextFieldsMap;
    }

    //delete field

    // get all field dto
    // get field dto by id
    // get field by id
}

