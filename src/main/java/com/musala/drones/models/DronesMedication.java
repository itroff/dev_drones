package com.musala.drones.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "drones_medicines")
@Table(name = "drones_medicines")
public class DronesMedication {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_drone")
    private Drone drone;

    @ManyToOne
    @JoinColumn(name = "id_medication")
    private Medication medication;

    @Column(name = "begin_dt")
    private LocalDateTime begin;

    @Column(name = "end_dt")
    private LocalDateTime end;
    
}
