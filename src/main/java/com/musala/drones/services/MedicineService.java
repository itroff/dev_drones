package com.musala.drones.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.drones.models.Medication;
import com.musala.drones.repositories.MedicineRepository;

@Service
public class MedicineService {

    @Autowired
    MedicineRepository medicineRepository;

    public List<Medication> checkingLoadedMedication(long idDrone) {

        return medicineRepository.findLoadedByIdDrone(idDrone).get();
    }


    
}
