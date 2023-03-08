package com.vms.model;
import jakarta.persistence.*;
import lombok.*;
import com.vms.model.enums.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FieldResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "form_submission_id", nullable = false)
    private FormSubmission formSubmission;
    
    @Column(name = "response_text")
    private String responseText;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    public static List<FieldResponse> toFieldResponseDTOs(List<FieldResponse> fieldResponses) {
        List<FieldResponse> fieldResponseDTOs = new ArrayList<>();
        for (FieldResponse fieldResponse : fieldResponses) {
            fieldResponseDTOs.add(FieldResponse.builder()
                .id(fieldResponse.getId())
                .field(fieldResponse.getField())
                .formSubmission(fieldResponse.getFormSubmission())
                .responseText(fieldResponse.getResponseText())
                .fieldType(fieldResponse.getFieldType())
                .build());
        }
        return fieldResponseDTOs;
    }

}
