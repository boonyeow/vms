package com.vms.service;

import com.vms.dto.FormSectionResponseDto;
import com.vms.dto.WorkflowDto;
import com.vms.dto.WorkflowFormDto;
import com.vms.dto.WorkflowResponseDto;
import com.vms.model.Form;
import com.vms.model.FormSection;
import com.vms.model.Workflow;
import com.vms.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkflowService {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private FormService formService;

    public void createWorkflow(WorkflowDto request) {
        Workflow workflow = Workflow.builder()
                .name(request.getName())
                .progress(0)
                .isDraft(true)
                .forms(new ArrayList<>())
                .formOrder(new ArrayList<>())
                .build();
        workflowRepository.save(workflow);
    }

    public void updateWorkflow(Long workflowId, WorkflowDto request) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id " + workflowId));
        if (!workflow.getIsDraft()) {
            throw new RuntimeException("Workflow that has been published cannot be updated");
        };
        List<Long> formOrder = request.getFormOrder();
        List<Long> formsId = request.getFormsId();
        if (formsId.size() != formOrder.size()) {
            throw new RuntimeException("Form Order and Forms size mismatch");
        }
        // Verify form stated in order exist in Form list
        for (Long id : formOrder) {
            if (!formsId.contains(id)) {
                throw new RuntimeException("Ordering contains Forms that is not found in the Workflow");
            }
        }
        // Build forms
        List<Form> forms = new ArrayList<>();
        for (Long id : formsId) {
            Form form = formService.getFormById(id);
            if (!form.isFinished()){
                throw new RuntimeException("Cannot add un-published Forms to Workflow");
            }
            forms.add(form);
        }

        workflow.setName(request.getName());
        workflow.setForms(forms);
        workflow.setFormOrder(request.getFormOrder());
        workflowRepository.save(workflow);
    }

    public void deleteWorkflow(Long workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id " + workflowId));
//        if (workflow.getIsDraft()) {
//            workflowRepository.delete(workflow);
//        } else {
//            throw new RuntimeException("Cannot delete Workflow that has already been published");
//        }
        workflowRepository.delete(workflow);
    }

    public WorkflowResponseDto getWorkflowDtoById(Long workflowId) {
        Workflow workflow = getWorkflowById(workflowId);
        List<Form> forms = workflow.getForms();
        List<WorkflowFormDto> workflowForms = new ArrayList<>();
        for (Form form: forms) {
            WorkflowFormDto workflowForm = WorkflowFormDto.builder()
                    .form_id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .build();
            workflowForms.add(workflowForm);
        }

        return WorkflowResponseDto.builder()
                .id(workflow.getId())
                .name(workflow.getName())
                .progress(workflow.getProgress())
                .isDraft(workflow.getIsDraft())
                .forms(workflowForms)
                .formOrder(workflow.getFormOrder())
                .build();
    }

    public Workflow getWorkflowById(Long workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found"));
        return workflow;
    }

}
