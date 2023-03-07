package com.vms.controller;

import com.vms.dto.FormResponseDto;
import com.vms.dto.FormSectionDto;
import com.vms.dto.WorkflowDto;
import com.vms.dto.WorkflowResponseDto;
import com.vms.model.Workflow;
import com.vms.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
    @Autowired
    private WorkflowService workflowService;

    // have to do a get All Workflow or at the very least do a get all workflow based on authorizedUser
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponseDto> getWorkflowById(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.getWorkflowDtoById(id));
    }
    @PostMapping
    public ResponseEntity<Void> createWorkflow(@RequestBody WorkflowDto request) {
        workflowService.createWorkflow(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkflow(@PathVariable Long id,
                                                  @RequestBody WorkflowDto request) {
        workflowService.updateWorkflow(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        workflowService.deleteWorkflow(id);
        return ResponseEntity.ok().build();
    }

}
