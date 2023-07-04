package com.musala.drones.repositories;

import com.musala.drones.models.LogEvent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EventsRepository extends CrudRepository<LogEvent, Long> {
    

    @Query("SELECT d FROM log_event d where subject=:sn and action = 'LOW_LEVEL_BATTERY'")
    Optional<List<LogEvent>> findByDroneSN(@Param("sn") String sn);
}
