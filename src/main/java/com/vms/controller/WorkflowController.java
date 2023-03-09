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

    @PostMapping
    public ResponseEntity<Void> createWorkflow() {
        workflowService.createWorkflow();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/authorizedAccount")
    public ResponseEntity<Void> addAuthorizedAccount(@PathVariable Long id,
                                                     @RequestParam Long accountId){
        workflowService.addAuthorizedAccount(id, accountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/authorizedAccount")
    public ResponseEntity<Void> removeAuthorizedAccount(@PathVariable Long id,
                                                        @RequestParam Long accountId){
        workflowService.removeAuthorizedAccount(id, accountId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/publishWorkflow")
    public ResponseEntity<Void> publishWorkflow(@PathVariable Long id){
        workflowService.publishWorkflow(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponseDto> getWorkflowById(@PathVariable Long id){
        return ResponseEntity.ok(workflowService.getWorkflowDtoById(id));
    }
}