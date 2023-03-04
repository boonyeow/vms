package com.vms.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormSectionDto {
    private Long id;
    private List<FieldDto> fields;
    private List<Long> authorizedAccountIds;
}
