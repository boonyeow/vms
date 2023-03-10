package com.vms.controller;

import com.vms.dto.FormResponseDto;
import com.vms.dto.FormSubmissionDto;
import com.vms.dto.FormSubmissionResponseDto;
import com.vms.model.enums.StatusType;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.FormSubmissionService;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formsubmission")
public class FormSubmissionController {
    @Autowired
    private FormSubmissionService formSubmissionService;

    @GetMapping("/getByAccountId")
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmissionBySubmitter(@RequestParam Long accountId){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsBySubmitter(accountId);
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getByWorkflowAndForm")
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmissionByWorkflowAndForm(@RequestParam Long workflowId,
                                                                                                 @RequestParam Long formId,
                                                                                                 @RequestParam int revisionNo){
        FormCompositeKey fck = new FormCompositeKey(formId, revisionNo);
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsByWorkflowAndForm(workflowId, fck);
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getByStatus")
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmissionByStats(@RequestParam StatusType status){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsByStatus(status);
        return ResponseEntity.ok(fsrDtoList);
    }

    @PostMapping()
    public ResponseEntity<Void> createFormSubmission(@RequestBody FormSubmissionDto request){
        formSubmissionService.createFormSubmission(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFormSubmissionStatus(@PathVariable Long id, @RequestBody StatusType status){
        formSubmissionService.updateFormSubmissionStatus(id, status);
        return ResponseEntity.ok().build();
    }

}
