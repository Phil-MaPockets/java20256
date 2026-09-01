package hr.javafx.model;

import jakarta.json.bind.annotation.JsonbTransient;

import java.math.BigDecimal;

public final class Economy extends Ticket{

    public Economy(){

    }
    public Economy(int id, int seatNumber, int airplaneID, BigDecimal price) {
        super(id, seatNumber, airplaneID, price);
    }

    @Override
    @JsonbTransient
    public String getClassName() {
        return "Economy";
    }
}
