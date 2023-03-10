package com.vms.controller;

import com.vms.dto.FormRequestDto;
import com.vms.dto.WorkflowDto;
import com.vms.dto.WorkflowResponseDto;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkflow(@PathVariable Long id,
                                               @RequestBody WorkflowDto request){
        workflowService.updateWorkflow(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/addForm")
    public ResponseEntity<Void> addFormToWorkflow(@PathVariable Long id,
                                                  @RequestBody FormRequestDto request){
        FormCompositeKey fck = FormCompositeKey.builder().id(request.getId()).revisionNo(request.getRevisionNo()).build();
        workflowService.addFormToWorkflow(fck, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/removeForm")
    public ResponseEntity<Void> removeFormFromWorkflow(@PathVariable Long id,
                                                       @RequestBody FormRequestDto request){
        FormCompositeKey fck = FormCompositeKey.builder().id(request.getId()).revisionNo(request.getRevisionNo()).build();
        workflowService.removeFormFromWorkflow(fck, id);
        return ResponseEntity.ok().build();
    }
}