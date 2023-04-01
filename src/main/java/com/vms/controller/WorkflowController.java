package com.vms.controller;

import com.vms.dto.*;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/workflows")
public class WorkflowController {
    @Autowired
    private WorkflowService workflowService;

    @GetMapping
    public ResponseEntity<List<WorkflowResponseDto>> getAllWorkflows() {
        return ResponseEntity.ok(workflowService.getWorkflowDtoList());
    }

    @PostMapping
    public ResponseEntity<Long> createWorkflow() {
        Long workflowId = workflowService.createWorkflow();
        return ResponseEntity.ok(workflowId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id){
        workflowService.removeWorkflow(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkflow(@PathVariable Long id,
                                               @RequestBody WorkflowDto request){
        workflowService.updateWorkflow(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateWorkflow/{id}")
    public ResponseEntity<Void> updateWorkflowEntirely(@PathVariable Long id,
                                               @RequestBody WorkflowUpdateDto request){
        workflowService.updateWorkflowEntirely(id, request);
        return ResponseEntity.ok().build();
    }


}