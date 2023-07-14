package com.subway.controllers;

import com.subway.services.SubwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class AppController {
    private final SubwayService subwayService;
    @Autowired
    public AppController(SubwayService subwayService) throws IOException {
        this.subwayService = subwayService;
        this.subwayService.getGasStations();
    }
}
