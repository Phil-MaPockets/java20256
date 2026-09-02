package hr.javafx.model;

import hr.javafx.exceptions.SeatAlreadyReservedException;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.annotation.JsonbTypeInfo;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonbTypeInfo(
        key = "ticketType",
        value = {
                @JsonbSubtype(alias = "FirstClass", type = FirstClass.class),
                @JsonbSubtype(alias = "BusinessClass", type = BusinessClass.class),
                @JsonbSubtype(alias = "Economy", type = Economy.class)
        }
)
public abstract sealed class Ticket implements Reservable, Serializable permits BusinessClass, Economy, FirstClass {
    private int id;
    private int seatNumber;
    private int trainID;
    private SeatStatus reserved;
    private BigDecimal price;

    protected Ticket(){
    }

    protected Ticket(int id, int seatNumber, int trainID, BigDecimal price) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.trainID = trainID;
        this.price = price;
        this.reserved = SeatStatus.UNRESERVED;
    }
    @JsonbTransient
    public abstract String getClassName();

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public void setReservation() throws SeatAlreadyReservedException {
        if(reserved.equals(SeatStatus.RESERVED)){
            throw new SeatAlreadyReservedException("Seat " + (getSeatNumber()+1) + " is already reserved");
        }
        this.reserved = SeatStatus.RESERVED;
    }
    @Override
    @JsonbTransient
    public SeatStatus isReserved() {
        return reserved;
    }

    public int getTrainID() {
        return trainID;
    }

    public void setTrainID(int trainID) {
        this.trainID = trainID;
    }
}
