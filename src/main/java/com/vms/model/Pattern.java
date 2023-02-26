package com.vms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pattern")
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String pattern;


    // Getters
    public String getName() {
        return name;
    }
    public String getPattern() {
        return pattern;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}