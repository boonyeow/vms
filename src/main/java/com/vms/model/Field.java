package com.vms.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vms.model.enums.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="field")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String label;
    private Boolean isRequired;
    private String helpText;

    // Omit pattern first
//    @Column(nullable = true)
//    private Pattern pattern;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Column(nullable = true)
    private List<String> options;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKey(name = "name")
    @JoinColumn(name = "next_field_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "next_field_id_fk"))
    private Map<String, Field> nextFields;

    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.fieldType = fieldType;
    }

    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType, ArrayList<String> options) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.fieldType = fieldType;
        this.options = options;
    }

    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType, ArrayList<String> options, Map<String, Field> nextFields) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.fieldType = fieldType;
        this.options = options;
        this.nextFields = nextFields;
    }

    @ManyToOne
    @JoinColumn(name="formsection_id")
    private FormSection formSection;
}