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
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class FieldService {
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private RegexService regexService;


    public Field createField(FieldRequestDto request, Form form){
        request.setFormCompositeKey(form.getId());
        if(request.getOptions() != null){
            Field field = Field.builder()
                    .name(request.getName())
                    .helpText(request.getHelpText())
                    .isRequired(request.getIsRequired())
                    .fieldType(request.getFieldType())
                    .form(form)
                    .build();
            Map<String, Field> nextFields = null;
            // check if options are not null
            Map<String, FieldRequestDto> optionsDto = request.getOptions();
            Set<String> optionKeys = optionsDto.keySet();
            nextFields = new HashMap<>();
            for(String option : optionKeys) {
                if (optionsDto.get(option) != null && optionsDto.get(option).getFieldType() == FieldType.TEXTBOX){
                    FieldRequestDto nextField = request.getOptions().get(option);
                    Field innerField = Field.builder()
                            .name(nextField.getName())
                            .helpText(nextField.getHelpText())
                            .isRequired(nextField.getIsRequired())
                            .fieldType(nextField.getFieldType())
                            .form(form)
                            .build();
                    Regex regex = regexService.getRegexById(nextField.getRegexId());
                    innerField.setRegex(regex);
                    fieldRepository.save(innerField);
                    nextFields.put(option, innerField);
                }
                else if(optionsDto.get(option) != null && optionsDto.get(option).getFieldType() != FieldType.TEXTBOX){
                    FieldRequestDto nextField = request.getOptions().get(option);
                    Field innerField = Field.builder()
                            .name(nextField.getName())
                            .helpText(nextField.getHelpText())
                            .isRequired(nextField.getIsRequired())
                            .fieldType(nextField.getFieldType())
                            .form(form)
                            .build();
                    Map<String, Field> innerNextFields = FieldDtotoField(nextField.getOptions());
                    System.out.println(innerNextFields);
                    //convert innerNextFields to String String
                    List<String> innerNextFieldsString = new ArrayList<>();
                    for (String innerOption : innerNextFields.keySet()) {
                        innerNextFieldsString.add(innerOption);
                    }
                    innerField.setOptionsAlternativeHolder(innerNextFieldsString);
                    innerField.setOptions(innerNextFields);
                    fieldRepository.save(innerField);
                    nextFields.put(option, innerField);
                }
                else{
                    nextFields.put(option, null);
                }

            }

            // check if nextField first key value is null
            if (nextFields.get(optionKeys.iterator().next()) == null){
                List<String> innerNextFieldsString = new ArrayList<>();
                for (String innerOption : nextFields.keySet()) {
                    innerNextFieldsString.add(innerOption);
                }
                field.setOptionsAlternativeHolder(innerNextFieldsString);
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
        if (options == null || options.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Field> nextFieldsMap = new HashMap<>();
        for (String option : options.keySet()) {
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
            Map<String, Long> nextFieldsId = field.getOptions().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getId()));
            if (nextFieldsId.isEmpty()){
                // get optionsAlternativeHolder and convert it to map <String, Long>
                List<String> optionsAlternativeHolder = field.getOptionsAlternativeHolder();
                if (optionsAlternativeHolder != null){
                    nextFieldsId = new HashMap<>();
                    for (String option : optionsAlternativeHolder) {
                        nextFieldsId.put(option, null);
                    }
                    System.out.println(nextFieldsId);
                    System.out.println("FUCKUASUDKCNK");

                }
            }
            fieldRequestDtos.add(convertToDto(field, nextFieldsId));
        }
        return fieldRequestDtos;
    }

    public FieldResponseDto getFieldDtoById(Long id){
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        //extract out the value of the field option in one line
        Map<String, Long> nextFieldsId = field.getOptions().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getId()));
        System.out.println("FUCKUASUDKCNK");
        // if nextFieldId is empty
        if (nextFieldsId.isEmpty()){
            // get optionsAlternativeHolder and convert it to map <String, Long>
            List<String> optionsAlternativeHolder = field.getOptionsAlternativeHolder();
            if (optionsAlternativeHolder != null){
                nextFieldsId = new HashMap<>();
                for (String option : optionsAlternativeHolder) {
                    nextFieldsId.put(option, null);
                }
                System.out.println(nextFieldsId);
                System.out.println("FUCKUASUDKCNK");

            }
        }

        return convertToDto(field, nextFieldsId);
    }

    public Field getFieldById(Long id){
        return fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
    }

    private FieldResponseDto convertToDto(Field field, Map<String, Long> nextFieldsId){
        if (nextFieldsId.isEmpty()){
            return FieldResponseDto.builder()
                    .id(field.getId())
                    .name(field.getName())
                    .helpText(field.getHelpText())
                    .isRequired(field.getIsRequired())
                    .fieldType(field.getFieldType())
                    .regexId(field.getRegex() == null ? null : field.getRegex().getId())
                    .formCompositeKey(field.getForm().getId())
                    .build();
        }
        return FieldResponseDto.builder()
                .id(field.getId())
                .name(field.getName())
                .helpText(field.getHelpText())
                .isRequired(field.getIsRequired())
                .fieldType(field.getFieldType())
                .nextFieldsId(nextFieldsId)
                .regexId(field.getRegex() == null ? null : field.getRegex().getId())
                .formCompositeKey(field.getForm().getId())
                .build();
    }

    public List<FieldResponseDto> getFieldResponseDtoList(List<Field> fields){
        List<FieldResponseDto> fieldResponseDtoList = new ArrayList<>();
        for(Field field: fields){
            Map<String, Long> nextFieldsId = field.getOptions().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getId()));
            if (nextFieldsId.isEmpty()){
                // get optionsAlternativeHolder and convert it to map <String, Long>
                List<String> optionsAlternativeHolder = field.getOptionsAlternativeHolder();
                if (optionsAlternativeHolder != null){
                    nextFieldsId = new HashMap<>();
                    for (String option : optionsAlternativeHolder) {
                        nextFieldsId.put(option, null);
                    }
                    System.out.println(nextFieldsId);
                    System.out.println("FUCKUASUDKCNK");

                }
            }
            fieldResponseDtoList.add(convertToDto(field, nextFieldsId));
        }
        return fieldResponseDtoList;
    }

    private Map<String, Long> getNextFieldsIdFromMap(Map<String, Field> nextFieldsMap){

        System.out.println("ENTRANCEEEEEE");

        System.out.println(nextFieldsMap);

        Map<String, Long> nextFieldsId = new HashMap<>();
        for (String option : nextFieldsMap.keySet()){
            System.out.println("HELLELEOINDZKLDNCKLASNLSNLKNKALNDKLANDLKNDKLNSLNASKLDNALKDNLASNDLSANLSDNLASNDLKSDN");
            System.out.println(option);
            Field nextField = nextFieldsMap.get(option);
            if(nextField != null){
                nextFieldsId.put(option, nextField.getId());
            }
            else {
                nextFieldsId.put(option, null);
            }
        }
        return nextFieldsId;
    }
}

