package com.vms.model.keys;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FormCompositeKey implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer revisionNo;

    public FormCompositeKey(Integer revisionNo) {
        this.revisionNo = revisionNo;
    }

//    public FormCompositeKey(Long id, Integer revisionNo) {
//        this.id = id;
//        this.revisionNo = revisionNo;
//    }

}
