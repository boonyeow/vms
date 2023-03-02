package com.vms.dto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDto {
    private String name;
    private String description;
    private boolean isFinished;
}