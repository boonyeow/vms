package com.vms.email;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDto {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}