package com.musala.drones.repositories;

import org.springframework.data.repository.CrudRepository;

import com.musala.drones.models.DronesMedication;

public interface DroneMedicationRepository extends CrudRepository<DronesMedication, Long>{
    
}
