package hr.javafx.model;

import jakarta.json.bind.annotation.JsonbTransient;

import java.math.BigDecimal;

public final class BusinessClass extends Ticket{

    public BusinessClass() {}
    public BusinessClass(int id, int seatNumber, int airplaneID, BigDecimal price) {
        super(id, seatNumber, airplaneID, price);
    }


    @Override
    @JsonbTransient
    public String getClassName() {
        return "BusinessClass";
    }
}
