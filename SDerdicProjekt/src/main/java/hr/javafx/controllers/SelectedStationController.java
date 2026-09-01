package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.data.UserChangeLogger;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.Address;
import hr.javafx.model.Station;

import hr.javafx.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectedStationController {
    private static final Logger log = LoggerFactory.getLogger(SelectedStationController.class);
    @FXML
    private TextField stationNameInput;
    @FXML
    private TextField stationStreetNameInput;
    @FXML
    private TextField stationStreetNumberInput;
    @FXML
    private TextField stationPostalCodeInput;
    @FXML
    private TextField stationCityNameInput;

    Station selectedStation;
    Station stationBefore;
    /**
     * Is Used as a method to pass on the Station that was selected on the StationController screen
     * @param station is the Station whose info loads into the fields on the screen
     */
    public void setSelectedStation(Station station){
        selectedStation = station;
        stationBefore =new Station(selectedStation);
        stationNameInput.setText(selectedStation.getStationName());
        stationStreetNameInput.setText(selectedStation.getAddress().streetName());
        stationStreetNumberInput.setText(selectedStation.getAddress().streetNumber());
        stationPostalCodeInput.setText(selectedStation.getAddress().postalCode());
        stationCityNameInput.setText(selectedStation.getAddress().cityName());
    }
    /**
     * Deletes the Station from the Database
     */
    public void saveChanges(){
        selectedStation.setStationName(stationNameInput.getText());
        selectedStation.setAddress(new Address(stationStreetNameInput.getText(),stationStreetNumberInput.getText(),stationPostalCodeInput.getText(),stationCityNameInput.getText()));
        DatabaseManager.updateStation(selectedStation);
        log.info("Updated station: {}", selectedStation.getStationName());
        UserChangeLogger.logChanged(TrainAdministrationApplication.getCurrentUser(), stationBefore, selectedStation);
    }
    /**
     * Saves any changes made to the Station
     */
    public void deleteStation(){
        if(!selectedStation.getTrains().isEmpty()){
            DialogUtils.cannotDeleteStation();
        }else{
            DatabaseManager.deleteStation(selectedStation);
            log.info("Deleted station: {}", selectedStation.getStationName());
            UserChangeLogger.logDelete(TrainAdministrationApplication.getCurrentUser(), selectedStation);
        }
    }
}
