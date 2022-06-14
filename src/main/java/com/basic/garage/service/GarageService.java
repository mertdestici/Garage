package com.basic.garage.service;

import com.basic.garage.model.Vehicle;

public interface GarageService {
    String registerVehicle(String vehicle);
    void deleteVehicle(int removingCar);
    String garageStatus();
    String textToDoAllTask(String text);
    Vehicle textToVehicleObject(String text);
}
