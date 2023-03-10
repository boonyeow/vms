package com.vms.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegexDto {
    private String name;
    private String pattern;
}
