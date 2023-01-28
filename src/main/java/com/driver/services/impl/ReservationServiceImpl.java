package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.*;
import com.driver.services.ReservationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Autowired
    PaymentServiceImpl paymentServiceImpl;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels)
            throws Exception {
        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        // Finding Minimum Price Spot
        List<Spot> spotList = parkingLot.getSpotList();
        int price = Integer.MAX_VALUE;
        int spotType = 0;
        Spot minimumPriceSpot = null;
        for (Spot s : spotList) {
            if (s.getSpotType().equals(SpotType.TWO_WHEELER)) {
                spotType = 2;
            } else if (s.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                spotType = 4;
            } else if (s.getSpotType().equals(SpotType.OTHERS)) {
                spotType = 100;
            }

            if (s.getPricePerHour() < price && spotType >= numberOfWheels && s.getOccupied() == false) {
                price = s.getPricePerHour();
                minimumPriceSpot = s;
            }
        }
        if (minimumPriceSpot == null) {
            throw new Exception("Cannot make reservation");
        }

        // for Reservation Repository
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(minimumPriceSpot);
        reservation.setUser(user);

        // for spot Repository
        minimumPriceSpot.setOccupied(true);
        List<Reservation> spotReservationList = minimumPriceSpot.getReservationList();
        if (spotReservationList == null) {
            spotReservationList = new ArrayList<>();
        }
        spotReservationList.add(reservation);
        minimumPriceSpot.setReservationList(spotReservationList);

        // for user Repository
        List<Reservation> userReservationList = user.getReservationList();
        if (userReservationList == null) {
            userReservationList = new ArrayList<>();
        }
        userReservationList.add(reservation);
        user.setReservationList(userReservationList);

        // for saving in all repository
        userRepository3.save(user);
        return reservation;

    }
}
