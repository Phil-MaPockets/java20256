package hr.javafx.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Station implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private transient List<Train> trains =new ArrayList<>();
    private String stationName;
    private transient Address address;

    public Station() {}

    public Station(int id, String stationName, Address address) {
        this.id = id;
        this.stationName = stationName;
        this.address = address;
    }
    public Station(Station station){
        this.id= station.getId();
        this.trains = station.getTrains();
        this.stationName= station.getStationName();
        this.address= station.address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Train> getTrains() {
        return trains;
    }

    public void setTrains(List<Train> trains) {
        this.trains = trains;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void addTrain(Train train) {
        trains.add(train);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}