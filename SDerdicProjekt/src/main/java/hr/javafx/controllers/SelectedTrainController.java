package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.data.UserChangeLogger;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.Station;
import hr.javafx.model.Train;
import hr.javafx.model.SeatStatus;
import hr.javafx.model.Ticket;
import hr.javafx.util.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static hr.javafx.util.MethodUtils.setComboBoxTime;

public class SelectedTrainController {
    private static final Logger log = LoggerFactory.getLogger(SelectedTrainController.class);
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
    Train selectedTrain;
    Train trainBefore;
    List<Station> stations = new ArrayList<>(DatabaseManager.getStations());

    public void initialize(){
        trainDepartureTimePicker.setItems(setComboBoxTime());
        trainArrivalTimePicker.setItems(setComboBoxTime());
        ObservableList<String> stationsList= FXCollections.observableList(stations.stream().map(Station::getStationName).toList());
        trainStationSelect.setItems(stationsList);
    }

    /**
     * Is Used as a method to pass on the Train that was selected on the TrainController screen
     * @param train is the Train whose info loads into the fields on the screen
     */
    public void setSelectedTrain(Train train){
        selectedTrain = train;
        trainBefore =new Train(selectedTrain);
        trainNameInput.setText(selectedTrain.getTrainName());
        trainDepartureDatePicker.setValue(selectedTrain.getDepartureTime().toLocalDate());
        trainDepartureTimePicker.setValue(selectedTrain.getDepartureTime().toLocalTime());
        trainArrivalDatePicker.setValue(selectedTrain.getArrivalTime().toLocalDate());
        trainArrivalTimePicker.setValue(selectedTrain.getArrivalTime().toLocalTime());
        trainStationSelect.setValue(stations.get(selectedTrain.getStationID()-1).getStationName());
    }

    /**
     * Deletes the Train from the Database
     */
    public void deleteTrain(){
        int counter=0;
        for(Ticket ticket: selectedTrain.getTickets()){
            if(ticket.isReserved().equals(SeatStatus.RESERVED)){
                DialogUtils.cannotDeleteTrain();
                counter++;
            }
        }
        if(counter==0){
            DatabaseManager.deleteTrain(selectedTrain);
            log.info("Deleted train: {}", selectedTrain.getTrainName());
            UserChangeLogger.logDelete(TrainAdministrationApplication.getCurrentUser(), selectedTrain);
        }
    }

    /**
     * Saves any changes made to the Train
     */
    public void saveChanges(){
        selectedTrain.setTrainName(trainNameInput.getText());
        for(Station station : stations){
            if(station.getStationName().equals(trainStationSelect.getValue())){
                selectedTrain.setStationID(station.getId());
            }
        }
        LocalDateTime departureDateTime = LocalDateTime.of(trainDepartureDatePicker.getValue(), trainDepartureTimePicker.getValue());
        LocalDateTime arrivalDateTime = LocalDateTime.of(trainArrivalDatePicker.getValue(),trainArrivalTimePicker.getValue());
        selectedTrain.setDepartureTime(departureDateTime);
        selectedTrain.setArrivalTime(arrivalDateTime);
        DatabaseManager.updateTrain(selectedTrain);
        log.info("Updated train: {}", selectedTrain.getTrainName());
        UserChangeLogger.logChanged(TrainAdministrationApplication.getCurrentUser(), trainBefore, selectedTrain);
    }
}
