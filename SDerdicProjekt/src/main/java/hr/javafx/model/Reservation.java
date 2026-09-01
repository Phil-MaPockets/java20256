package hr.javafx.model;

import java.time.LocalDateTime;

public class Reservation<U extends User, T extends Ticket>
{
    private final U user;
    private final T ticket;
    private final LocalDateTime reservationDate;

    public Reservation(U user, T ticket){
        this.user = user;
        this.ticket = ticket;
        this.reservationDate = LocalDateTime.now();
    }
    public U getUser(){
        return this.user;
    }
    public T getTicket(){
        return this.ticket;
    }
    public LocalDateTime getReservationDate(){
        return this.reservationDate;
    }
}
