package com.musala.drones.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Entity(name = "medication")
@Table(name = "medication")
public class Medication {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Pattern(regexp = "[a-zA-Z0-9\\-_]+")
    @NotBlank
    private String name;

    @Column
    @Min(0)
    private int weight;

    @Column
    @Pattern(regexp = "[A-Z0-9_]+")
    @NotBlank
    private String code;

    // link to file
    @Column
    private String image;
    
}
