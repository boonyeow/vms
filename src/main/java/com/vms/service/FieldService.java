package com.vms.service;

import com.vms.dto.FieldDto;
import com.vms.model.Field;
import com.vms.model.enums.FieldType;
import com.vms.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


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

    public void deleteField(Long id){
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        fieldRepository.delete(field);
    }

    public List<FieldDto> getAllFieldsDto(){
        Iterable<Field> fields = fieldRepository.findAll();
        List<FieldDto> fieldDtos = new ArrayList<>();
        for(Field field : fields){
            fieldDtos.add(convertToDto(field));
        }
        return fieldDtos;
    }

    public FieldDto getFieldDtoById(Long id){
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        return convertToDto(field);
    }

    public Field getFieldById(Long id){
        return fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
    }

    private FieldDto convertToDto(Field field){
        return FieldDto.builder()
                .name(field.getName())
                .label(field.getLabel())
                .helpText(field.getHelpText())
                .isRequired(field.getIsRequired())
                .fieldType(field.getFieldType())
                .options(field.getOptions())
                .nextFieldsId(getNextFieldsIdFromMap(field.getNextFields()))
                .build();
    }

    private Map<String, Long> getNextFieldsIdFromMap(Map<String, Field> nextFieldsMap){
        Map<String, Long> nextFieldsId = new HashMap<>();
        for(Map.Entry<String, Field> entry : nextFieldsMap.entrySet()){
            nextFieldsId.put(entry.getKey(), entry.getValue().getId());
        }
        return nextFieldsId;
    }
}

