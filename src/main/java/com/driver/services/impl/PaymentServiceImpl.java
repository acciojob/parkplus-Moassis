package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        int pricePerHour = reservation.getSpot().getPricePerHour();
        int numberOfHours = reservation.getNumberOfHours();
        int bill = numberOfHours * pricePerHour;
        if (amountSent < bill) {
            throw new Exception("Insufficient Balance");
        }
        char[] arr = mode.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 'a' && arr[i] <= 'z') {
                arr[i] = (char) (arr[i] + 'A' - 'a');
            }
        }
        String resultMode = new String(arr);

        // for Payment Repository
        Payment payment = new Payment();
        if (resultMode.equals("CASH")) {
            payment.setPaymentMode(PaymentMode.CASH);
        } else if (resultMode.equals("CARD")) {
            payment.setPaymentMode(PaymentMode.CARD);
        } else if (resultMode.equals("UPI")) {
            payment.setPaymentMode(PaymentMode.UPI);
        } else {
            throw new Exception("Payment mode not detected");
        }
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);

        // for Reservation Repository
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);

        return payment;
    }
}
