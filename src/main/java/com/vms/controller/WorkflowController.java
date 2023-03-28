package com.vms.controller;

import com.vms.dto.*;
import com.vms.model.Workflow;
import com.vms.model.enums.AccountType;
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
        Long workflowId= workflowService.createWorkflow();
        return ResponseEntity.ok(workflowId);
    }

    @PostMapping("/{id}/authorizedAccount")
    public ResponseEntity<Void> addAuthorizedAccount(@PathVariable Long id,
                                                     @RequestParam Long accountId){
        workflowService.addAuthorizedAccount(id, accountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/authorizedAccount")
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

    @GetMapping("/getWorkflowsByAccountId/{id}")
    public ResponseEntity<List<WorkflowResponseDto>> getWorkflowsByAccountId(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.getWorkflowDtoByAccountId(id));
    }

    @GetMapping("/getWorkflowsByAccountType/{accountType}")
    public ResponseEntity<List<WorkflowAccountTypeDto>> getWorkflowsByAccountId(@PathVariable AccountType accountType) {
        return ResponseEntity.ok(workflowService.getWorkflowDtoByAccountType(accountType));
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

    @PostMapping("/{id}/addMultipleForms")
    public ResponseEntity<Void> addFormsToWorkflow(@PathVariable Long id,
                                                  @RequestBody List<FormRequestDto> requests){
        for (FormRequestDto request : requests) {
            FormCompositeKey fck = FormCompositeKey.builder().id(request.getId()).revisionNo(request.getRevisionNo()).build();
            workflowService.addFormToWorkflow(fck, id);
        }
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