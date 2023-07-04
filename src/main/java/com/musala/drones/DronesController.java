package com.musala.drones;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.musala.drones.models.Drone;
import com.musala.drones.models.Medication;
import com.musala.drones.models.RequestDrone;
import com.musala.drones.models.RequestLoadingDrone;
import com.musala.drones.models.Status;
import com.musala.drones.models.exceptions.DroneException;
import com.musala.drones.services.DroneService;
import com.musala.drones.services.MedicineService;

@RestController
public class DronesController {
    

    @Autowired
    DroneService droneService;

    @Autowired
    MedicineService medicineService;

    @PostMapping("/registering-drone")
    public ResponseEntity<Drone> registeringDrone(@Valid @RequestBody Drone drone,
                                                         Errors errors) {
        if (errors.hasErrors()) {
            throw new DroneException(errors.toString());
        }
        if (!droneService.registeringDrone(drone)) {
            throw new DroneException("error while saving drone");
        }
        
        return new ResponseEntity<>(drone, HttpStatus.OK);
    }


    @PostMapping("/loading-drone")
    public ResponseEntity<Status> loadingDrone(@RequestBody RequestLoadingDrone request) {

        if (!droneService.loadingDrone(request.getIdDrone(), request.getIdMedication())){
            return new ResponseEntity<>(new Status("ERROR", "overweight"), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(new Status("OK", "all loaded"), HttpStatus.OK);
    }

    @PostMapping("/checking-loaded-medication")
    public ResponseEntity<List<Medication>> checkingLoaded(@RequestBody RequestDrone request) {

        return new ResponseEntity<>(medicineService.checkingLoadedMedication(request.getIdDrone()), HttpStatus.OK);
        
    }

    @GetMapping("/checking-available-drones")
    public ResponseEntity<List<Drone>> checkingAvailable(){

        return new ResponseEntity<>(droneService.checkingAvailableDrones(), HttpStatus.OK);
    }

    @PostMapping("/check-drone-battery")
    public ResponseEntity<Integer> checkDroneBattery(@RequestBody RequestDrone request) {
        return new ResponseEntity<>(droneService.checkDroneBatteryLevel(request.getIdDrone()), HttpStatus.OK);
    }
}
