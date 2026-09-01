package hr.javafx.controllers;
import hr.javafx.data.DatabaseManager;
import hr.javafx.data.UserChangeLogger;
import hr.javafx.exceptions.DuplicateTrainException;
import hr.javafx.exceptions.InvalidScheduleInputException;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.Station;
import hr.javafx.model.Train;
import hr.javafx.util.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static hr.javafx.util.MethodUtils.setComboBoxTime;

public class TrainInputController {
    private static final Logger log = LoggerFactory.getLogger(TrainInputController.class);
    @FXML
    private ComboBox<String> trainStationSelect;
    @FXML
    private TextField trainNameInput;
    @FXML
    private DatePicker trainDepartureDatePicker;
    @FXML
    private ComboBox<LocalTime> trainDepartureTimePicker;
    @FXML
    private DatePicker trainArrivalDatePicker;
    @FXML
    private ComboBox<LocalTime> trainArrivalTimePicker;

    List<Station> stations = new ArrayList<>(DatabaseManager.getStations());
    List<Train> trains = new ArrayList<>(DatabaseManager.getTrains());

    public void initialize(){
        trainDepartureTimePicker.setItems(setComboBoxTime());
        trainArrivalTimePicker.setItems(setComboBoxTime());
        ObservableList<String> stationsList= FXCollections.observableList(stations.stream().map(Station::getStationName).toList());
        trainStationSelect.setItems(stationsList);
    }

    /**
     * Performs the insertion of an train based on the information selected and written in the fields above
     */
    public void insertTrain(){
        try {
            String trainName=trainNameInput.getText();
            LocalDate departureDate=trainDepartureDatePicker.getValue();
            LocalTime departureTime=trainDepartureTimePicker.getValue();
            LocalDate arrivalDate=trainArrivalDatePicker.getValue();
            LocalTime arrivalTime=trainArrivalTimePicker.getValue();
            String selectedStation=trainStationSelect.getValue();
            List<String> emptyFields=new ArrayList<>();

            if(selectedStation==null || selectedStation.isEmpty()) emptyFields.add("Train Station");
            if (trainName == null || trainName.trim().isEmpty()) emptyFields.add("Train Name");
            if (departureDate == null) emptyFields.add("Departure Date");
            if (departureTime == null) emptyFields.add("Departure Time");
            if (arrivalDate == null)   emptyFields.add("Arrival Date");
            if (arrivalTime == null)   emptyFields.add("Arrival Time");

            if(!emptyFields.isEmpty()){
                DialogUtils.missingInputAlert(emptyFields);
                log.info("Inserting a new train failed because some fields remain empty");
            }
            else {
                LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);
                LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate,arrivalTime);
                if(arrivalDateTime.isBefore(departureDateTime)){
                    throw new InvalidScheduleInputException("Inserting a new train failed because the arrival time is before the Departure time");
                }
                int trainStationID = stations.stream().filter(station -> station.getStationName().matches(trainStationSelect.getValue())).findFirst().map(Station::getId).orElseThrow(() -> new IllegalArgumentException("Station not found"));
                Train newTrain = new Train.Builder().id(trains.size()).trainName(trainName).stationID(trainStationID).build();
                newTrain.generateTickets();
                newTrain.setDepartureTime(departureDateTime);
                newTrain.setArrivalTime(arrivalDateTime);
                DatabaseManager.insertTrain(newTrain);
                DatabaseManager.insertTickets(newTrain.getTickets());

                UserChangeLogger.logCreate(TrainAdministrationApplication.getCurrentUser(), newTrain);
            }
        } catch (InvalidScheduleInputException | DuplicateTrainException | IOException | SQLException e) {
            if(e instanceof DuplicateTrainException) DialogUtils.duplicateInput("Train");
            log.warn(e.getMessage());
        }
    }
}
