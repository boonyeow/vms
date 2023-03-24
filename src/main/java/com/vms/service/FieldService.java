package com.vms.service;

import com.vms.dto.AccountDto;
import com.vms.dto.FieldRequestDto;
import com.vms.dto.FieldResponseDto;
import com.vms.model.Account;
import com.vms.model.Field;
import com.vms.model.Form;
import com.vms.model.Regex;
import com.vms.model.keys.FormCompositeKey;
import com.vms.model.enums.FieldType;
import com.vms.repository.FieldRepository;
import com.vms.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FieldService {
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private RegexService regexService;

    public Field createField(FieldRequestDto request, Form form){
        request.setFormCompositeKey(form.getId());
        if(request.getOptions() != null){
            System.out.println("hellooooooooooooooooooooooooooooooooooooooooooooooooo");
            Field field = Field.builder()
                    .name(request.getName())
                    .helpText(request.getHelpText())
                    .isRequired(request.getIsRequired())
                    .fieldType(request.getFieldType())
                    .form(form)
                    .build();
            System.out.println("hellooooooooooooooooooooooooooooooooooooooooooooooooo");
            Map<String, Field> nextFields = null;
            // check if options are not null
            Map<String, FieldRequestDto> optionsDto = request.getOptions();
            Set<String> optionKeys = optionsDto.keySet();
            if(optionsDto.get(optionKeys.iterator().next()) != null &&
                    optionsDto.get(optionKeys.iterator().next()).getFieldType() == FieldType.TEXTBOX){

                System.out.println("babibuasbdkjaskjnakdjnkadsjkadssdsdsssss");
                nextFields = new HashMap<>();
                for(String option : optionKeys){
                    System.out.println(option);
                    FieldRequestDto nextField = request.getOptions().get(option);
                    Field innerField = Field.builder()
                            .name(nextField.getName())
                            .helpText(nextField.getHelpText())
                            .isRequired(nextField.getIsRequired())
                            .fieldType(nextField.getFieldType())
                            .form(form)
                            .build();

                    if(nextField.getFieldType().equals(FieldType.TEXTBOX)){
                        Regex regex = regexService.getRegexById(nextField.getRegexId());
                        innerField.setRegex(regex);
                    }
                    fieldRepository.save(innerField);
                    nextFields.put(option, innerField);
                }
            }
            else{

                System.out.println("hellooooooooooooooooooooooooooooooooooooooooooooooooaaaaaaaaao");
                nextFields = new HashMap<>();
                for(String option : optionKeys) {
                    FieldRequestDto nextField = request.getOptions().getOrDefault(option, null);

                    if(nextField == null){
                        System.out.println("heihe");
                        nextFields.put(option, null);
                        continue;
                    }

                    Field innerField = Field.builder()
                            .name(nextField.getName())
                            .helpText(nextField.getHelpText())
                            .isRequired(nextField.getIsRequired())
                            .fieldType(nextField.getFieldType())
                            .form(form)
                            .build();
                    innerField.setOptions(FieldDtotoField(nextField.getOptions()));
                    fieldRepository.save(innerField);
                    nextFields.put(option, innerField);
                }
            }

            field.setOptions(nextFields);
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

    private Map<String, Field> FieldDtotoField(Map<String, FieldRequestDto> options) {
        Map<String, Field> nextFieldsMap = new HashMap<>();
        for(String option : options.keySet()){
            nextFieldsMap.put(option, null);
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
                .id(field.getId())
                .name(field.getName())
                .helpText(field.getHelpText())
                .isRequired(field.getIsRequired())
                .fieldType(field.getFieldType())
                .nextFieldsId(getNextFieldsIdFromMap(field.getOptions()))
                .regexId(field.getRegex() == null ? null : field.getRegex().getId())
                .formCompositeKey(field.getForm().getId())
                .build();
    }

    public List<FieldResponseDto> getFieldResponseDtoList(List<Field> fields){
        List<FieldResponseDto> fieldResponseDtoList = new ArrayList<>();
        for(Field field: fields){
            fieldResponseDtoList.add(convertToDto(field));
        }
        return fieldResponseDtoList;
    }

    private Map<String, Long> getNextFieldsIdFromMap(Map<String, Field> nextFieldsMap){
        Map<String, Long> nextFieldsId = new HashMap<>();
        for (String option : nextFieldsMap.keySet()){
            System.out.println(option);
            Field nextField = nextFieldsMap.get(option);
            if(nextField != null){
                nextFieldsId.put(option, nextField.getId());
            }
        }
        return nextFieldsId;
    }
}

