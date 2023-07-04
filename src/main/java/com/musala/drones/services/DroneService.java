package com.musala.drones.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.musala.drones.models.Drone;
import com.musala.drones.models.DronesMedication;
import com.musala.drones.models.LogEvent;
import com.musala.drones.models.Medication;
import com.musala.drones.repositories.DroneMedicationRepository;
import com.musala.drones.repositories.DroneRepository;
import com.musala.drones.repositories.MedicineRepository;

@Service
public class DroneService {

    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    DroneMedicationRepository droneMedicationRepository;

    @Autowired
    EventService log;

    public boolean registeringDrone(Drone drone) {
        try{
            droneRepository.save(drone);
        } catch(Exception ex) {
            return false;
        }
        log.addEvent(LogEvent.Event.REGISTERING_DRONE, drone.getSerialNumber() , "/registering-drone");
        return true;
    }

    @Transactional
    public boolean loadingDrone(long idDrone, long[] idMedicine) {
        Optional<Drone> drone = droneRepository.findById(idDrone);
        if(drone.isEmpty()){
            return false;
        }
        if (drone.get().getState() != Drone.State.IDLE && drone.get().getState() != Drone.State.LOADING) {
            return false;
            //throw new DroneException("Drone is not loading state");
        }
        if(drone.get().getBatteryCapacity() < 25) {
            return false;
        }
        int weight = 0;
        for (long id : idMedicine) {
            Medication med = medicineRepository.findById(id).get();
            if(weight + med.getWeight() > drone.get().getWeightLimit()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
               // throw new DroneException("Overweight");
            } else {
                weight += med.getWeight();
            }
            DronesMedication dm = new DronesMedication();
            dm.setDrone(drone.get());
            dm.setMedication(med);
            dm.setBegin(LocalDateTime.now());
            droneMedicationRepository.save(dm);
        }
        drone.get().setState(Drone.State.LOADING);
        droneRepository.save(drone.get());
        return true;
    }

    public List<Drone> checkingAvailableDrones() {
        List<Drone> lst = droneRepository.findAvailable().get();

        return lst;
    }

    public int checkDroneBatteryLevel(long idDrone){
        return droneRepository.findById(idDrone).get().getBatteryCapacity();
    }

    /**
     * Periodically checkig drones for audit log
     */
    @Scheduled(fixedDelay = 5000)
    public void checkingBattery() {
        List<Drone> drones = droneRepository.findLowLevelBattery().get();

        for (Drone drone : drones) {
            drone.setState(Drone.State.IDLE);
            droneRepository.save(drone);
            // TODO unload medications marking end date to drone medicines

            log.addEvent(LogEvent.Event.LOW_LEVEL_BATTERY, drone.getSerialNumber(), "scheduled checkingBattery");
        }
    }
    
}
