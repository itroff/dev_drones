package com.musala.drones.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.musala.drones.models.Medication;

public interface MedicineRepository  extends CrudRepository<Medication, Long>{
    
     @Query("SELECT m FROM drones_medicines d left join d.medication m where d.drone.id=:id and d.end is null")
     Optional<List<Medication>> findLoadedByIdDrone(@Param("id") Long id);
}
