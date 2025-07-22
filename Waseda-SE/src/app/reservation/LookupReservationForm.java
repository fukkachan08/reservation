package app.reservation;

import app.AppException;
import domain.reservation.Reservation;

/**
 * Form class for looking up a reservation
 */
public class LookupReservationForm {

    private LookupReservationControl control = new LookupReservationControl();
    private String reservationNumber;

    private LookupReservationControl getLookupReservationControl() {
        return control;
    }

    public Reservation findReservation() throws AppException {
        LookupReservationControl c = getLookupReservationControl();
        return c.findReservation(reservationNumber);
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
}
