package com.musala.drones.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.drones.models.LogEvent;
import com.musala.drones.repositories.EventsRepository;

@Service
public class EventService {

    @Autowired
    EventsRepository repository;

    public void addEvent(LogEvent.Event event, String object, String path){
        LogEvent lg = new LogEvent();
        lg.setAction(event);
        lg.setDate(LocalDateTime.now());
        lg.setSubject(object);
        lg.setPath(path);
        repository.save(lg);
    }
    
}
