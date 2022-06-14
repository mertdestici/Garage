package com.basic.garage.service.impl;

import com.basic.garage.model.Vehicle;
import com.basic.garage.service.GarageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class GarageServiceImpl implements GarageService {

    private static int MAX_GARAGE_SLOT = 10;

    private List<Vehicle> vehicleList = new ArrayList<>();
    private int[] slotList = new int[MAX_GARAGE_SLOT];

    public GarageServiceImpl() {
        for (int i = 0; i < MAX_GARAGE_SLOT; i++) {
            slotList[i] = 0;
        }
    }

    @Override
    public String registerVehicle(String vehicleText) {
        Vehicle vehicle = textToVehicleObject(vehicleText);
        final boolean[] checkLicence = {true};
        if (checkAvailableGarageSlot(vehicle.getSlot())) {
            vehicleList.forEach(v -> {
                if (v.getLicencePlate().equalsIgnoreCase(vehicle.getLicencePlate())) {
                    checkLicence[0] = false;
                }
            });
            if (checkLicence[0]) {
                if (vehicleList.isEmpty()) {
                    int i;
                    for (i = 0; i < vehicle.getSlot(); i++) {
                        slotList[i] = 1;
                    }
                    slotList[i] = 2;
                } else {
                    int i;
                    int counter = 0;
                    for (i = 0; i < slotList.length; i++) {
                        if (slotList[i] == 2 && vehicleList.size() == 1) {
                            break;
                        } else if (slotList[i] == 2) {
                            counter++;
                            if (counter == vehicleList.size()){
                                break;
                            }
                        }
                    }
                    i++;
                    if (i + vehicle.getSlot() < slotList.length) {
                        if (slotList[i + 1] == 0 && slotList[i + vehicle.getSlot()] == 0) {
                            for (int j = 0; j < vehicle.getSlot(); j++) {
                                slotList[i] = 1;
                                i++;
                            }
                        }
                        if (i != slotList.length){
                            slotList[i] = 2;
                        }
                    }
                }

                System.out.println("Array : " + Arrays.toString(slotList));
                vehicleList.add(vehicle);
                return "Allocated " + vehicle.getSlot() + " slot" + (vehicle.getSlot() > 1 ? "s.\n" : ".\n");
            } else {
                return "Licence has already inside.\n";
            }

        } else {
            return "Garage is full\n";
        }

    }

    @Override
    public void deleteVehicle(int removingCar) {
        if (removingCar > MAX_GARAGE_SLOT) {
            log.error("Removing car number is higher than max garage slot number.");
            return;
        }
        if (removingCar > vehicleList.size()) {
            log.error("Removing car number is not in vehicle list.");
            return;
        }

        vehicleList.remove(removingCar - 1);
        log.info("Removing car complete.");
    }

    @Override
    public String garageStatus() {
        String status = "Status : \n";
        for (Vehicle vehicle : vehicleList) {
            status = String.join(" ", status, vehicle.getLicencePlate(), vehicle.getColor(), "\n");
        }
        return status;
    }

    @Override
    public String textToDoAllTask(String text) {
        String response = "";
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] strings = line.split(" ");
            switch (strings.length) {
                case 1:
                    log.info("status");
                    response = String.join("", response, garageStatus());
                    break;
                case 2:
                    log.info("leave");
                    deleteVehicle(Integer.parseInt(strings[1]));
                    break;
                case 4:
                    log.info("park");
                    String vehicle = String.join(" ", strings[1], strings[2], strings[3]);
                    response = String.join("", response, registerVehicle(vehicle));
                    break;
                default:
                    break;
            }
        }
        return response;
    }

    @Override
    public Vehicle textToVehicleObject(String text) {
        String[] strings = text.split(" ");
        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate(strings[1]);
        vehicle.setColor(strings[2]);
        vehicle.setVehicleType(strings[3]);
        vehicle.setSlot(getVehicleSlot(vehicle));
        return vehicle;
    }

    private int getVehicleSlot(Vehicle vehicle) {
        int slot = 0;
        switch (vehicle.getVehicleType()) {
            case "Car":
                slot = 1;
                break;
            case "Truck":
                slot = 4;
                break;
            case "Jeep":
                slot = 2;
                break;
            default:
                slot = 0;
                break;
        }
        return slot;
    }

    private boolean checkAvailableGarageSlot(int slot) {
        boolean check = true;
        int garageSlot = 0;

        for (Vehicle vehicle : vehicleList) {
            garageSlot += vehicle.getSlot();
        }
        int counter = 0;
        for (int i = 0; i < slotList.length; i++) {
            if (slotList[i] == 0){
                counter++;
            }
        }
        if(counter < slot){
            return false;
        }

        int availableSlot = MAX_GARAGE_SLOT - garageSlot;
        if (availableSlot < slot) {
            check = false;
        }
        return check;
    }
}
