package com.vms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vms.dto.FieldDto;
import com.vms.model.FormSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vms.model.Field;
import com.vms.repository.FieldRepository;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FormService formService;

    @Autowired
    private FormSectionService formSectionService;

    public Iterable<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    public Optional<Field> getFieldById(Long id) {
        return fieldRepository.findById(id);
    }

    public void createField(Long formId, Long formSectionId, FieldDto request) {
        FormSection formSection = formSectionService.getFormSectionById(formSectionId);
        List<Field> fields = formSection.getFormFields();

        Map<String, FieldDto> nextFieldsDto = request.getNextFields();
        Map<String, Field> nextFields = new HashMap<>();
        if(!nextFieldsDto.isEmpty()){
            for(String option : nextFieldsDto.keySet()){
                FieldDto currentFieldDto = nextFieldsDto.get(option);
                Field currentField = Field.builder()
                        .name(currentFieldDto.getName())
                        .label(currentFieldDto.getLabel())
                        .helpText(currentFieldDto.getHelpText())
                        .isRequired(currentFieldDto.isRequired())
                        .fieldType(currentFieldDto.getFieldType())
                        .options(currentFieldDto.getOptions())
                        .nextFields(new HashMap<>()).build();
                nextFields.put(option, currentField);
            }
        }

        Field newField = Field.builder()
                .name(request.getName())
                .label(request.getLabel())
                .fieldType(request.getFieldType())
                .options(request.getOptions())
                .nextFields(nextFields)
                        .build();
        fieldRepository.save(newField);
        formSection.setFormFields(fields);
    }

    public Field updateField(Long id, Field field) {
        field.setId(id);
        return fieldRepository.save(field);
    }

    public void deleteField(Long id) {
        fieldRepository.deleteById(id);
    }
}