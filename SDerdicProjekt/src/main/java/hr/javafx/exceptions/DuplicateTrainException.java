package hr.javafx.exceptions;

public class DuplicateTrainException extends Exception {
    public DuplicateTrainException(String airplaneID) {
        super("Station with the same name already exists: " + airplaneID);
    }
}
