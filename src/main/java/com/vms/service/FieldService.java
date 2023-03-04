package com.vms.service;

import com.vms.dto.FieldDto;
import com.vms.model.Field;
import com.vms.model.FormSection;
import com.vms.model.enums.FieldType;
import com.vms.repository.FieldRepository;
import com.vms.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FieldService {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FormSectionService formSectionService;
    public void createField(Long formId, Long formSectionId, FieldDto request){
        FormSection formSection = formSectionService.getFormSectionById(formSectionId);

        Map<String, Field> nextFields = getNextFieldsFromDto(request.getNextFields());
        Field newField = Field.builder()
                .name(request.getName())
                .label(request.getLabel())
                .isRequired(request.isRequired())
                .helpText(request.getHelpText())
                .fieldType(request.getFieldType())
                .options(request.getOptions())
                .nextFields(nextFields)
                .formSection(formSection)
                .build();
        fieldRepository.save(newField);
        List<Field> currentFields = formSection.getFields();
        currentFields.add(newField);
        formSection.setFields(currentFields);
        formSectionService.saveFormSection(formSection);
    }

    private Map<String, Field> getNextFieldsFromDto(Map<String, Long> nextFieldsDto){
        Map<String, Field> nextFields = new HashMap<>();
        if(nextFieldsDto != null){
            // nextFields property set
            for(String option : nextFieldsDto.keySet()){
                Long linkedFieldId = nextFieldsDto.get(option);
                Field linkedField = getFieldById(linkedFieldId); // Ensures it exists before linking
                if(linkedField.getFieldType() != FieldType.TEXTBOX){
                    // i.e. Complex field linked with complex field linked with complex field
                    throw new RuntimeException("Recursive structure found");
                }
                nextFields.put(option, linkedField);
            }
        }
        return nextFields;
    }

    public Field getFieldById(Long fieldId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        return field;
    }
}
