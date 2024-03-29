package com.vms.controller;

import com.vms.dto.FormResponseDto;
import com.vms.dto.FormSubmissionDto;
import com.vms.dto.FormSubmissionResponseDto;
import com.vms.dto.FormSubmissionUpdateDto;
import com.vms.model.enums.StatusType;
import com.vms.model.keys.FormCompositeKey;
import com.vms.service.FormSubmissionService;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/formsubmission")
public class FormSubmissionController {
    @Autowired
    private FormSubmissionService formSubmissionService;

    @GetMapping()
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmission(){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getAllFormSubmissions();
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getById")
    public ResponseEntity<List<FormSubmissionResponseDto>> getFormSubmissionById(@RequestParam String formSubmissionId){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsById(Long.parseLong(formSubmissionId));
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getByAccountId")
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmissionBySubmitter(@RequestParam String accountId){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsBySubmitter(Long.parseLong(accountId));
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getByWorkflowAndForm")
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmissionByWorkflowAndForm(@RequestParam String workflowId,
                                                                                                 @RequestParam String formId,
                                                                                                 @RequestParam String revisionNo){
        FormCompositeKey fck = new FormCompositeKey(Long.parseLong(formId), Integer.parseInt(revisionNo));
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsByWorkflowAndForm(Long.parseLong(workflowId), fck);
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getByStatus")
    public ResponseEntity<List<FormSubmissionResponseDto>> getAllFormSubmissionByStats(@RequestParam StatusType status){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsByStatus(status);
        return ResponseEntity.ok(fsrDtoList);
    }

    @GetMapping("/getByReviewerId")
    public ResponseEntity<List<FormSubmissionResponseDto>> getFormSubmissionsByReviewer(@RequestParam String accountId){
        List<FormSubmissionResponseDto> fsrDtoList = formSubmissionService.getFormSubmissionsByReviewer(Long.parseLong(accountId));
        return ResponseEntity.ok(fsrDtoList);
    }

    @PostMapping()
    public ResponseEntity<Void> createFormSubmission(@RequestBody FormSubmissionDto request){
        formSubmissionService.createFormSubmission(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/UpdateFormSubmission")
    public ResponseEntity<Void> updateFormSubmission(@PathVariable Long id, @RequestBody FormSubmissionDto request){
        System.out.println(id);System.out.println(id);System.out.println(id);
        formSubmissionService.updateFormSubmission(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFormSubmissionStatus(@PathVariable Long id, @RequestBody FormSubmissionUpdateDto status){
        formSubmissionService.updateFormSubmissionStatus(id, status.getStatus(), status.getAccountId());
        return ResponseEntity.ok().build();
    }

}
