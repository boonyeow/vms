package com.vms.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    public boolean sendSimpleEmail(EmailDto request){
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(sender);
            msg.setTo(request.getRecipient());
            msg.setText(request.getMsgBody());
            msg.setSubject(request.getSubject());
            javaMailSender.send(msg);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public boolean sendEmailWithAttachment(EmailDto request) {
        // Construct a mime message and declare helper
        MimeMessage mimeMsg = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true); // Set true to support alternative texts, inline elements and attachments

            FileSystemResource file = new FileSystemResource(new File(request.getAttachment()));

            helper.setFrom(sender);
            helper.setTo(request.getRecipient());
            helper.setText(request.getMsgBody());
            helper.setSubject(request.getSubject());
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMsg);
            return true;
        } catch (MessagingException e){
            return false;
        }

    }
}
