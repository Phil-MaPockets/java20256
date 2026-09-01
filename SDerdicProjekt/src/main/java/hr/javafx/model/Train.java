package hr.javafx.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Train implements Schedulable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private int stationID;
    private String trainName;
    private transient List<Ticket> tickets=new ArrayList<>();
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;


    public Train(Builder builder){
        this.id=builder.id;
        this.stationID=builder.stationID;
        this.trainName=builder.trainName;
    }
    public Train(Train train){
        this.id= train.getId();
        this.stationID= train.getStationID();
        this.trainName= train.getTrainName();
        this.tickets= train.getTickets();
        this.departureTime= train.getDepartureTime();
        this.arrivalTime= train.getArrivalTime();
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Train(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    @Override
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public void setDepartureTime(LocalDateTime departureTime){
        this.departureTime=departureTime;
    }

    @Override
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public void setArrivalTime(LocalDateTime arrivalTime){
        this.arrivalTime=arrivalTime;
    }
    public static class Builder {
        private int id;
        private int stationID;
        private String trainName;
        public Builder id(int id) {
            this.id = id;
            return this;
        }
        public Builder stationID(int stationID) {
            this.stationID = stationID;
            return this;
        }
        public Builder trainName(String trainName) {
            this.trainName = trainName;
            return this;
        }
        public Train build(){
            return new Train(this);
        }
    }

    /**
     * generates the List of tickets inside the Train class
     */
    public void generateTickets() {
        for (int j = 0; j < 4; j++) {
            switch (j) {
                case 0 ->
                    tickets.add(new FirstClass(tickets.size(), j, this.id, BigDecimal.valueOf(1000).add(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(1000)))));
                case 1 ->
                    tickets.add(new BusinessClass(tickets.size(), j, this.id, BigDecimal.valueOf(500).add(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(500)))));
                default ->
                    tickets.add(new Economy(tickets.size(), j, this.id, BigDecimal.valueOf(100).add(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(100)))));
            }
        }
    }
    public List<Ticket> getTrainTickets(){
        return this.tickets;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", trainName='" + trainName + '\'' +
                '}';
    }
}
