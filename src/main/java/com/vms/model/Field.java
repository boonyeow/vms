package com.vms.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import com.vms.model.enums.*;

@NoArgsConstructor
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
    
    @Column(nullable = true)
    private String regexPattern;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Column(nullable = true)
    private ArrayList<String> options;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKey(name = "name")
    @JoinColumn(name = "next_field_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "next_field_id_fk"))
    private Map<String, Field> nextFieldMap;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<FieldResponse> responses = new ArrayList<>();

    // textbox type to hv regex pattern
    public Field(String name, String label, Boolean isRequired, String helpText, String regexPattern, FieldType fieldType) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.regexPattern = regexPattern;
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

    public Field(String name, String label, Boolean isRequired, String helpText, FieldType fieldType, ArrayList<String> options, Map<String, Field> nextFieldMap) {
        this.name = name;
        this.label = label;
        this.isRequired = isRequired;
        this.helpText = helpText;
        this.fieldType = fieldType;
        this.options = options;
        this.nextFieldMap = nextFieldMap;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public String getHelpText() {
        return helpText;
    }

    public List<FieldResponse> getResponses() {
        return responses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public Map<String, Field> getNextFieldMap() {
        return nextFieldMap;
    }

    public void setNextField(Map<String, Field> nextFieldMap) {
        this.nextFieldMap = nextFieldMap;
    }
}
