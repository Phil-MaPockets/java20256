package hr.javafx.model;

import hr.javafx.exceptions.SeatAlreadyReservedException;

public sealed interface Reservable permits Ticket {
    void setReservation() throws SeatAlreadyReservedException;
    SeatStatus isReserved();

}
