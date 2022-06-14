package com.basic.garage.controller;

import com.basic.garage.service.impl.GarageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GarageController {

    private final GarageServiceImpl garageService;

    @PostMapping("garage")
    public String incommingText(@RequestBody String vehicle){
        return garageService.textToDoAllTask(vehicle);
    }

    @PostMapping("/park")
    public String incommingVehicle(@RequestBody String vehicle){
        return garageService.registerVehicle(vehicle);
    }

    @GetMapping("/status")
    public String garageStatus(){
        return garageService.garageStatus();
    }

    @PostMapping("/leave/{removedVehicle}")
    public void deleteVehicle( @PathVariable String removedVehicle){
        garageService.deleteVehicle(Integer.parseInt(removedVehicle));
    }


}
