package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.data.UserChangeLogger;
import hr.javafx.exceptions.DuplicateStationException;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.Address;
import hr.javafx.model.Station;
import hr.javafx.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationInputController {
    private static final Logger log = LoggerFactory.getLogger(StationInputController.class);
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

    List<Station> stations = DatabaseManager.getStations();

    /**
     * Performs the insertion of a station based on the information selected and written in the fields above
     */
    public void insertStation(){
        try{
            List<String> emptyFields=new ArrayList<>();
            String stationName=stationNameInput.getText();
            String stationStreetName=stationStreetNameInput.getText();
            String stationStreetNumber=stationStreetNumberInput.getText();
            String stationPostalCode=stationPostalCodeInput.getText();
            String stationCityName=stationCityNameInput.getText();

            if (stationName == null || stationName.trim().isEmpty()) emptyFields.add("Station Name");
            if (stationStreetName == null || stationStreetName.trim().isEmpty()) emptyFields.add("Street Name");
            if (stationStreetNumber == null || stationStreetNumber.trim().isEmpty()) emptyFields.add("Street Number");
            if (stationPostalCode == null || stationPostalCode.trim().isEmpty()) emptyFields.add("Postal Code");
            if (stationCityName == null || stationCityName.trim().isEmpty()) emptyFields.add("City Name");

            if(!emptyFields.isEmpty()){
                DialogUtils.missingInputAlert(emptyFields);
                log.info("Inserting a new station failed because some fields remain empty");
            }else{
                Address address=new Address(stationStreetName,stationStreetNumber,stationPostalCode,stationCityName);
                Station newStation =new Station(stations.size(),stationName,address);
                DatabaseManager.insertStation(newStation);
                stations.add(newStation);

                UserChangeLogger.logCreate(TrainAdministrationApplication.getCurrentUser(), newStation);
            }
        }catch (IOException | SQLException | DuplicateStationException e) {
            DialogUtils.duplicateInput("Station");
            log.error(e.getMessage());
        }
    }
}
