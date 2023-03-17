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
        if(request.getOptionsWithNextFields() != null){
            Field field = Field.builder()
                    .name(request.getName())
                    .helpText(request.getHelpText())
                    .isRequired(request.getIsRequired())
                    .fieldType(request.getFieldType())
                    .form(form)
                    .build();

            Map<String, Field> nextFields = null;
            // check if options are not null
            if(request.getOptionsWithNextFields().get(request.getOptionsWithNextFields().keySet().iterator().next()).getFieldType() == FieldType.TEXTBOX){
                nextFields = new HashMap<>();
                for(String option : request.getOptionsWithNextFields().keySet()){
                    FieldRequestDto nextField = request.getOptionsWithNextFields().get(option);
                    Field newField = createField(nextField, form);
                    nextFields.put(option, newField);
                }
            }
            else{
                nextFields = new HashMap<>();
                for(String option : request.getOptionsWithNextFields().keySet()) {
                    FieldRequestDto nextField = request.getOptionsWithNextFields().get(option);
                    Field innerField = Field.builder()
                            .name(nextField.getName())
                            .helpText(nextField.getHelpText())
                            .isRequired(nextField.getIsRequired())
                            .fieldType(nextField.getFieldType())
                            .form(form)
                            .build();
                    innerField.setOptionsWithNextFields(FieldDtotoField(nextField.getOptionsWithNextFields()));
                    fieldRepository.save(innerField);
                    nextFields.put(option, innerField);
                }
            }

            field.setOptionsWithNextFields(nextFields);
            fieldRepository.save(field);
            return field;
        }

        Field field = Field.builder()
                .name(request.getName())
                .helpText(request.getHelpText())
                .isRequired(request.getIsRequired())
                .fieldType(request.getFieldType())
                .form(form)
                .build();

        if(request.getFieldType().equals(FieldType.TEXTBOX)){
            Regex regex = regexService.getRegexById(request.getRegexId());
            field.setRegex(regex);
        }

        fieldRepository.save(field);
        return field;
    }

    private Map<String, Field> FieldDtotoField(Map<String, FieldRequestDto> optionsWithNextFields) {
        Map<String, Field> nextFieldsMap = new HashMap<>();
        if (optionsWithNextFields != null){
            for(String option : optionsWithNextFields.keySet()){
                nextFieldsMap.put(option, null);
            }
        }
        return nextFieldsMap;
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
                .nextFieldsId(getNextFieldsIdFromMap(field.getOptionsWithNextFields()))
                .regexId(field.getRegex() == null ? null : field.getRegex().getId())
                .formCompositeKey(field.getForm().getId())
                .build();
    }
    private Map<String, Long> getNextFieldsIdFromMap(Map<String, Field> nextFieldsMap){
        Map<String, Long> nextFieldsId = new HashMap<>();
        for (String option : nextFieldsMap.keySet()){
            Field nextField = nextFieldsMap.get(option);
            if(nextField != null){
                nextFieldsId.put(option, nextField.getId());
            }
        }
        return nextFieldsId;
    }
}

