package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        // for spot Repository
        Spot spot = new Spot();
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);
        if (numberOfWheels <= 2) {
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels > 2 || numberOfWheels <= 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        } else {
            spot.setSpotType(SpotType.OTHERS);
        }

        // for parkingLot Repository
        List<Spot> spotList = parkingLot.getSpotList();
        if (spotList == null) {
            spotList = new ArrayList<>();
        }
        spotList.add(spot);
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        // Delete a specific spot from a parking lot.

        // for ParkingLot Repository
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = spot.getParkingLot();
        List<Spot> spotList = parkingLot.getSpotList();
        ListIterator<Spot> itr = spotList.listIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(spot)) {
                itr.remove();
            }
        }
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);

        // for Spot Repository
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        // Update the details of a specific spot within a parking lot, including the
        // price per hour.

        // for Spot Repository
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);

        // for parkingLot Repository
        List<Spot> spotList = parkingLot.getSpotList();
        if (spotList == null) {
            spotList = new ArrayList<>();
        }
        spotList.add(spot);
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        // Delete a specific parking lot and all spots within it.
        parkingLotRepository1.deleteById(parkingLotId);

    }
}
