package com.vms.service;

import com.vms.dto.FieldRequestDto;
import com.vms.dto.FieldResponseDto;
import com.vms.model.Field;
import com.vms.model.Form;
import com.vms.model.Regex;
import com.vms.model.keys.FormCompositeKey;
import com.vms.model.enums.FieldType;
import com.vms.repository.FieldRepository;
import com.vms.repository.FormRepository;
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

    @Autowired
    private RegexService regexService;

    public Field createField(FieldRequestDto request, Form form){
        // save the created field id and pass it into FieldDto to create the current field
        Map<String, Field> nextFields = null;
        if(request.getNextFields() != null){
            nextFields = new HashMap<>();
            for(String option : request.getNextFields().keySet()){
                FieldRequestDto nextField = request.getNextFields().get(option);
                // save the created field id and pass it into FieldDto to create the current field
                if (nextField.getFieldType() != FieldType.TEXTBOX && nextField.getOptions() != null && nextField.getNextFields() != null){
                    throw new RuntimeException("Recursive structure found");
                }
                // create and save next field here
                Field newField = Field.builder()
                        .name(nextField.getName())
                        .helpText(nextField.getHelpText())
                        .isRequired(nextField.getIsRequired())
                        .fieldType(nextField.getFieldType())
                        .form(form)
                        .build();

                if(nextField.getFieldType().equals(FieldType.TEXTBOX)){
                    Regex regex = regexService.getRegexById(nextField.getRegexId());
                    newField.setRegex(regex);
                }
                fieldRepository.save(newField);
                nextFields.put(option, newField);
            }
        }

        Field field = Field.builder()
                .name(request.getName())
                .helpText(request.getHelpText())
                .isRequired(request.getIsRequired())
                .fieldType(request.getFieldType())
                .options(request.getOptions())
                .nextFields(nextFields)
                .form(form)
                .build();

        if(request.getFieldType().equals(FieldType.TEXTBOX)){
            Regex regex = regexService.getRegexById(request.getRegexId());
            field.setRegex(regex);
        }

        fieldRepository.save(field);
        return field;
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

    public List<FieldResponseDto> getAllFieldsDto(){
        Iterable<Field> fields = fieldRepository.findAll();
        List<FieldResponseDto> fieldRequestDtos = new ArrayList<>();
        for(Field field : fields){
            fieldRequestDtos.add(convertToDto(field));
        }
        return fieldRequestDtos;
    }

    public FieldResponseDto getFieldDtoById(Long id){
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        return convertToDto(field);
    }

    public Field getFieldById(Long id){
        return fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
    }

    private FieldResponseDto convertToDto(Field field){
        return FieldResponseDto.builder()
                .name(field.getName())
                .helpText(field.getHelpText())
                .isRequired(field.getIsRequired())
                .fieldType(field.getFieldType())
                .options(field.getOptions())
                .nextFieldsId(getNextFieldsIdFromMap(field.getNextFields()))
                .regexId(field.getRegex() == null ? null : field.getRegex().getId())
                .formCompositeKey(field.getForm().getId())
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

