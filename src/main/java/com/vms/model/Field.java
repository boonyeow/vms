package com.vms.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import com.vms.model.enums.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="field")
public class Field{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String label;
    private Boolean isRequired;
    private String helpText;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @ManyToOne
    @Nullable
    @JoinColumn(name = "regex_id")
    private Regex regex;
    @Column(nullable = true)
    private List<String> options;

//    @OneToMany(cascade = CascadeType.ALL)
//    @MapKey(name = "name")
//    @JoinColumn(name = "next_field_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "next_field_id_fk"))
//    private Map<String, Field> nextFields;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "form_id", referencedColumnName = "id"),
            @JoinColumn(name = "form_revision_no", referencedColumnName = "revisionNo")
    })
    private Form form;

    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType, Form form) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.fieldType = fieldType;
        this.form = form;
    }

    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType, ArrayList<String> options, Form form) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.fieldType = fieldType;
        this.options = options;
        this.form = form;
    }

//    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType, ArrayList<String> options, Map<String, Field> nextFields, Form form) {
//        this.name = name;
//        this.label = label;
//        this.isRequired = isRequired;
//        this.helpText = helpText;
//        this.fieldType = fieldType;
//        this.options = options;
//        this.nextFields = nextFields;
//        this.form = form;
//    }
}
