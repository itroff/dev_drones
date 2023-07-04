package com.musala.drones.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "")
public class DroneException  extends RuntimeException{
    // private String message;

    public DroneException(String message) {
        super(message);
    }
}
