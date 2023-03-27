package com.vms.email;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    // Sending a simple Email
    @PostMapping("/sendSimpleEmail")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDto request) {
        if(emailService.sendSimpleEmail(request)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // Sending email with attachment
    @PostMapping("/sendEmailWithAttachment")
    public ResponseEntity<Void> sendEmailWithAttachment(@RequestBody EmailDto request) {
        if(emailService.sendEmailWithAttachment(request)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
