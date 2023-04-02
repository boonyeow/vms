package com.vms.controller;
import com.vms.dto.*;
import com.vms.model.WorkflowFormAssignment;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.WorkflowFormAssignmentService;
import com.vms.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/workflowFormAssignment")
public class WorkflowFormAssignmentController {
    @Autowired
    private WorkflowFormAssignmentService workflowFormAssignmentService;

    @GetMapping("/getUnsubmitted/{accountId}")
    public ResponseEntity<List<WorkflowFormAssignmentResponseDto>> getAllFormSubmission(@PathVariable Long accountId){
        return ResponseEntity.ok(workflowFormAssignmentService.findWorkflowFormAssignmentsNotInFormSubmissionByAccountId(accountId));
    }

    @GetMapping("/getUnsubmitted")
    public ResponseEntity<List<WorkflowFormAssignmentResponseDto>> getAllUnsubmittedForm() {
        return ResponseEntity.ok(workflowFormAssignmentService.findWorkflowFormAssignmentsNotInFormSubmission());
    }
}
