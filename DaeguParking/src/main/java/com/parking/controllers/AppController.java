package com.parking.controllers;

import com.parking.services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class AppController {
    private final ParkingService parkingService;
    @Autowired
    public AppController(ParkingService parkingService) throws IOException {
        this.parkingService = parkingService;
        this.parkingService.getData();
    }
}
