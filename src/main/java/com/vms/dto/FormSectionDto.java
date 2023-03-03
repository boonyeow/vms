package com.vms.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormSectionDto {
    private Long id;
    private List<Long> authorizedAccountIds;
}
