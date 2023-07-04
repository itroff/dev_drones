package com.musala.drones.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.musala.drones.models.Drone;

public interface DroneRepository extends CrudRepository<Drone, Long>{
    

    @Query("SELECT d FROM drones d where (d.state = 'IDLE' or d.state = 'LOADING') and d.batteryCapacity >= 25")
    Optional<List<Drone>> findAvailable();

    @Query("SELECT d FROM drones d where d.batteryCapacity < 25")
    Optional<List<Drone>> findLowLevelBattery();
}
