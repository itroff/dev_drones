package com.musala.drones;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.musala.drones.models.Drone;
import com.musala.drones.models.LogEvent;
import com.musala.drones.models.Medication;
import com.musala.drones.models.Drone.DroneModel;
import com.musala.drones.repositories.DroneMedicationRepository;
import com.musala.drones.repositories.DroneRepository;
import com.musala.drones.repositories.EventsRepository;
import com.musala.drones.repositories.MedicineRepository;
import com.musala.drones.services.DroneService;
import com.musala.drones.services.MedicineService;

@SpringBootTest
class DronesApplicationTests {

	@Autowired
	private DroneService droneService;

	@Autowired
	private DroneRepository droneRepository;

	@Autowired
	private MedicineService medicineService;

	@Autowired
	private MedicineRepository medicineRepository;


	@Autowired
	private DroneMedicationRepository droneMedicationRepository;


	@Autowired
	private EventsRepository eventsRepository;

	private  Validator validator;

	@BeforeEach
	public void validator() {
		 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    	validator = factory.getValidator();
	}

	private <T> boolean checkViolation(T obj) {
		Set<ConstraintViolation<T>> violations = validator.validate(obj);
		return violations.isEmpty();
	}

	@Test
	void testDroneModel(){
		Drone drone = new Drone();
		drone.setModel(DroneModel.Lightweight);
		drone.setState(Drone.State.IDLE);
		
		// check serial number constraints
		drone.setSerialNumber(null);

		Set<ConstraintViolation<Drone>> violations = validator.validate(drone);
		assertFalse(violations.isEmpty());

		drone.setSerialNumber("0".repeat(120));
		violations = validator.validate(drone);
		assertFalse(violations.isEmpty());

		drone.setSerialNumber("123");
		violations = validator.validate(drone);
		assertTrue(violations.isEmpty());

		// check battery capacity
		drone.setBatteryCapacity(101);
		violations = validator.validate(drone);
		assertFalse(violations.isEmpty());

		drone.setBatteryCapacity(-1);
		violations = validator.validate(drone);
		assertFalse(violations.isEmpty());

		drone.setBatteryCapacity(30);
		violations = validator.validate(drone);
		assertTrue(violations.isEmpty());

		//check model
		drone.setModel(null);
		assertFalse(checkViolation(drone));

		drone.setModel(Drone.DroneModel.Middleweight);
		// check weight limit
		drone.setWeightLimit(-1);
		assertFalse(checkViolation(drone));

		drone.setWeightLimit(501);
		assertFalse(checkViolation(drone));

		drone.setWeightLimit(100);
		assertTrue(checkViolation(drone));

		// check state
		drone.setState(null);
		assertFalse(checkViolation(drone));

	}

	@Test
	void testMedicationModel(){
		Medication medication = new Medication();
		medication.setCode("ABC");
		
		//check name
		medication.setName(null);
		assertFalse(checkViolation(medication));

		medication.setName("123-.");
		assertFalse(checkViolation(medication));

		medication.setName("123-_");
		assertTrue(checkViolation(medication));

		//check code
		medication.setCode(null);
		assertFalse(checkViolation(medication));

		medication.setCode("abc");
		assertFalse(checkViolation(medication));

		medication.setCode("ABC_123");
		assertTrue(checkViolation(medication));

	}

	@Test
	@DirtiesContext
	void testRegisteringDrone(){
		Drone drone = new Drone();
		drone.setModel(Drone.DroneModel.Heavyweight);
		drone.setState(Drone.State.IDLE);
		drone.setSerialNumber("123");
		boolean register  = droneService.registeringDrone(drone); 
		assertTrue(register);
		drone.setSerialNumber(null);
		register  = droneService.registeringDrone(drone); 
		assertFalse(register);
	}

	@Test
	@DirtiesContext
	void testLoadingDrone() {
		boolean result = droneService.loadingDrone(1L, new  long[]{1,2,3});
		assertFalse(result);

		result = droneService.loadingDrone(1L, new  long[]{1,1,1,1,1});
		assertTrue(result);
		assertTrue(droneMedicationRepository.count() == 5);
		Optional<Drone> drone  = droneRepository.findById(1L);
		assertTrue(drone.get().getState().equals(Drone.State.LOADING));

		result = droneService.loadingDrone(21L, new  long[]{1,1,1,1,1});
		assertFalse(result);

		// prevent loading min than 25 battery
		result = droneService.loadingDrone(3L, new  long[]{1});
		assertFalse(result);
	}

	@Test
	@DirtiesContext
	void testLoaded(){
		boolean result = droneService.loadingDrone(1L, new  long[]{1,1,1,1,1});
		assertTrue(result);
		List<Medication> lst = medicineService.checkingLoadedMedication(1L);
		assertTrue(lst.size() == 5);
	}


	@Test
	@DirtiesContext
	void testAvailable(){
		List<Drone> lst = droneService.checkingAvailableDrones();
		assertTrue(lst.size() == 8);
	}

	@Test
	@DirtiesContext
	void testBAttery(){
		int level = droneService.checkDroneBatteryLevel(3L);
		assertTrue(level == 1);
	}


	@Test
	@DirtiesContext
	void countIntitialData(){
		assertTrue(droneRepository.count() == 10);
		assertTrue(medicineRepository.count() == 3);
	}

	@Test
	@DirtiesContext
	void testLowLevel(){
		Drone drone = droneRepository.findById(1L).get();
		drone.setBatteryCapacity(24);
		droneRepository.save(drone);

		droneService.checkingBattery();
		List<LogEvent> lg = eventsRepository.findByDroneSN(drone.getSerialNumber()).get();
		assertTrue(lg.size() > 0);
	}

}
