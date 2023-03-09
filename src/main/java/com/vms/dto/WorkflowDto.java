package com.vms.dto;

import com.vms.model.Form;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowDto {
    private Long id;

    private String name;

    private boolean isFinal;
}
