package com.musala.drones.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity(name = "drones")
@Table(name = "drones")
public class Drone {

    public enum DroneModel {Lightweight, Middleweight,Cruiserweight, Heavyweight };
    
    public enum State {IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING};
    
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, name = "serial_number")
    @NotBlank
    @Length(max = 100)
    private String serialNumber;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Column(name = "weight_limit")
    @Min(0)
    @Max(500)
    private int weightLimit;

    @Column(name = "battery_capacity")
    @Min(0)
    @Max(100)
    private int batteryCapacity;

    @Column(columnDefinition = "varchar(255) default 'IDLE'")
    @Enumerated(EnumType.STRING)
    @NotNull
    private State state;

    
}
