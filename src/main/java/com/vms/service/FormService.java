package com.vms.service;

import com.vms.dto.*;
import com.vms.model.Account;
import com.vms.model.Field;
import com.vms.model.Form;
import com.vms.model.Workflow;
import com.vms.model.keys.FormCompositeKey;
import com.vms.repository.FormRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormService {
    @Autowired
    private FormRepository formRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FieldService fieldService;

    public Form duplicateForm(FormCompositeKey fck) {
        Form form = getFormByFck(fck);
        Integer currentRevisionNo = formRepository.findByFormId_Id(fck.getId()).stream()
                .mapToInt(f -> f.getId().getRevisionNo())
                .max()
                .orElseThrow(() -> new RuntimeException("surprise"));

        Form duplicatedForm = Form.builder()
                .id(new FormCompositeKey(fck.getId(), currentRevisionNo + 1))
                .name(form.getName())
                .description(form.getDescription())
                .isFinal(false)
                .fields(new ArrayList<>())
                .build();
        formRepository.save(duplicatedForm);
        List<Field> duplicatedFields = fieldService.duplicateFields(form.getFields(), duplicatedForm);
        duplicatedForm.setFields(duplicatedFields);

        formRepository.save(duplicatedForm);
        return duplicatedForm;
    }

    @Transactional
    public void updateForm(FormCompositeKey fck, FormDto request) {
        Form form = getFormByFck(fck);

        if(form.isFinal()){
            throw new RuntimeException("Form is final. No more changes are allowed");
        }

        List<Field> currentFields = form.getFields();
        for (Field field : currentFields) {
            deleteFieldWithEntityManager(field);
        }
        form.setName(request.getName());
        form.setDescription(request.getDescription());
        form.setFinal(request.isFinal());
        List<FieldRequestDto> fieldRequestDtoList = request.getFields();

        List<Field> fields = new ArrayList<>();
        for (FieldRequestDto fieldRequestDto : fieldRequestDtoList) {
            Field field = fieldService.createField(fieldRequestDto, form);
            fields.add(field);
        }

        form.setFields(fields);

        formRepository.save(form);
    }

    public void deleteFieldWithEntityManager(Field field) {
        entityManager.remove(field);
    }

    public void deleteForm(FormCompositeKey fck) {
        Form form = getFormByFck(fck);
        if (!form.getWorkflows().isEmpty()) {
            throw new RuntimeException("Unable to delete form due to referential violation");
        }
        if (form.isFinal()) {
            throw new RuntimeException("Unable to delete form that is final");
        }
        formRepository.delete(form);
    };

    public List<FormResponseDto> getAllFormDto() {
        Iterable<Form> forms = formRepository.findAll();
        List<FormResponseDto> formResponses = new ArrayList<>();
        for (Form form : forms) {
            List<Long> workflowIds = getWorkflowIds(form.getWorkflows());

            FormResponseDto formResponseDto = FormResponseDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .workflows(workflowIds)
                    .build();
            formResponses.add(formResponseDto);
        }
        return formResponses;
    }

    public List<Long> getWorkflowIds(Set<Workflow> workflows) {
        List<Long> workflowIds = new ArrayList<>();
        for (Workflow workflow : workflows) {
            workflowIds.add(workflow.getId());
        }
        return workflowIds;
    }

    public List<FormResponseDto> getFormDtoByState(Boolean state) {
        Iterable<Form> forms = formRepository.findFormByState(state);
        List<FormResponseDto> formResponses = new ArrayList<>();
        for (Form form : forms) {
            List<Long> workflowIds = getWorkflowIds(form.getWorkflows());
            FormResponseDto formResponseDto = FormResponseDto.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .isFinal(form.isFinal())
                    .workflows(workflowIds)
                    .build();
            formResponses.add(formResponseDto);
        }
        return formResponses;
    }

    public FormResponseDto getFormDtoByFck(FormCompositeKey fck) {
        Form form = getFormByFck(fck);

        List<Long> workflowIds = getWorkflowIds(form.getWorkflows());
        return FormResponseDto.builder()
                .id(form.getId())
                .name(form.getName())
                .description(form.getDescription())
                .isFinal(form.isFinal())
                .workflows(workflowIds)
                .build();
    }

    public Form getFormByFck(FormCompositeKey fck) {
        return formRepository.findById(fck)
                .orElseThrow(() -> new RuntimeException("Form not found"));
    }

    public List<FieldResponseDto> getFieldsByFck(FormCompositeKey fck) {
        Form form = getFormByFck(fck);
        List<Field> fields = form.getFields();
        List<FieldResponseDto> fieldResponseDtos = new ArrayList<>();
        for (Field field : fields) {
            Map<String, Long> nextFieldsId = field.getOptions().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getId()));
            // get optionsAlternativeHolder and convert it to map <String, Long>
            List<String> optionsAlternativeHolder = field.getOptionsAlternativeHolder();
            if (optionsAlternativeHolder != null) {
                for (String option : optionsAlternativeHolder) {
                    nextFieldsId.put(option, null);
                }
                System.out.println(nextFieldsId);

            }
            System.out.println(nextFieldsId);
            fieldResponseDtos.add(convertToDto(field, nextFieldsId));
        }
        return fieldResponseDtos;
    }

    private FieldResponseDto convertToDto(Field field, Map<String, Long> nextFieldsId) {
        if (nextFieldsId.isEmpty()) {
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

    // Create form and return FormCompositeKey
    public FormCompositeKey createAndGetFck() {
        FormCompositeKey fck = FormCompositeKey.builder().revisionNo(1).build();
        Form form = Form.builder()
                .id(fck)
                .name("Untitled Form")
                .description("")
                .isFinal(false)
                .workflows(new HashSet<>())
                .fields(new ArrayList<>())
                .build();
        formRepository.saveWithAutoGeneratedId(form);
        return form.getId();
    }

    public FormCompositeKey duplicateAndGetFck(FormCompositeKey fck) {
        Form form = duplicateForm(fck);
        return form.getId();
    }
}
