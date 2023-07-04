package com.musala.drones.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity(name = "log_event")
@Table(name = "log_event")
public class LogEvent {

    public enum Event {REGISTERING_DRONE, LOW_LEVEL_BATTERY};

    @Id
    @Column
    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime date;

    @Column
    @Enumerated(EnumType.STRING)
    private Event action;

    @Column
    private String subject;

    @Column
    private String path;

    
}
