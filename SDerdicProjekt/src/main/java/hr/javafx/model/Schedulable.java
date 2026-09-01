package hr.javafx.model;

import java.time.LocalDateTime;

public interface Schedulable {
    void setDepartureTime(LocalDateTime departureTime);
    LocalDateTime getDepartureTime();
    void setArrivalTime(LocalDateTime arrivalTime);
    LocalDateTime getArrivalTime();
}
