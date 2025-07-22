package app.reservation;

import app.AppException;
import app.ManagerFactory;
import domain.reservation.Reservation;
import domain.reservation.ReservationException;
import domain.reservation.ReservationManager;

/**
 * Control class for looking up reservations
 */
public class LookupReservationControl {

    public Reservation findReservation(String reservationNumber) throws AppException {
        try {
            ReservationManager manager = getReservationManager();
            return manager.getReservation(reservationNumber);
        } catch (ReservationException e) {
            AppException ex = new AppException("Failed to lookup reservation", e);
            ex.getDetailMessages().add(e.getMessage());
            ex.getDetailMessages().addAll(e.getDetailMessages());
            throw ex;
        }
    }

    private ReservationManager getReservationManager() {
        return ManagerFactory.getInstance().getReservationManager();
    }
}
