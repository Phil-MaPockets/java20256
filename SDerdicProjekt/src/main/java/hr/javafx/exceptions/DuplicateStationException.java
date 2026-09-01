package hr.javafx.exceptions;

public class DuplicateStationException extends Exception{
    public DuplicateStationException(String airplaneID) {
        super("Station with the same name already exists: " + airplaneID);
    }
}
