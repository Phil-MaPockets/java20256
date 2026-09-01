package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.Station;
import hr.javafx.util.DialogUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


public class StationController {
    private static final Logger log = LoggerFactory.getLogger(StationController.class);
    @FXML
    private Label streetNumberLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private TextField streetName;
    @FXML
    private TextField streetNumber;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField city;
    @FXML
    private TextField stationName;
    @FXML
    private TableView<Station> stationTableView;
    @FXML
    private TableColumn<Station,String> stationNameColumn;
    @FXML
    private TableColumn<Station,String> stationStreetNameColumn;
    @FXML
    private TableColumn<Station,String> stationStreetNumberColumn;
    @FXML
    private TableColumn<Station,String> stationCityColumn;
    @FXML
    private TableColumn<Station,String> stationPostalCodeColumn;
    List<Station> stations = DatabaseManager.getStations();

    public void initialize(){
        ObservableList<Station> stationObservableList = FXCollections.observableArrayList(stations);
        stationTableView.setItems(stationObservableList);
        stationNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getStationName())));
        stationStreetNameColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getAddress().streetName()));
        stationStreetNumberColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getAddress().streetNumber()));
        stationCityColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getAddress().cityName()));
        stationPostalCodeColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getAddress().postalCode()));

        stationTableView.setRowFactory(view->{TableRow<Station> row=new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2&&!row.isEmpty()&& TrainAdministrationApplication.isAdmin()){
                    Station selectedStation =row.getItem();
                    openSelectedStation(selectedStation);
                }
            });
            return row;
        });
    }

    /**
     * Searches through all the stations based of the criteria written in the fields above
     */
    public void searchStations(){
        String stationNameFilter=stationName.getText();
        String stationStreetNumberFilter=streetNumber.getText();
        String stationCityFilter=city.getText();
        String stationPostalCodeFilter=postalCode.getText();
        String stationStreetNameFilter=streetName.getText();
        if(!stationStreetNumberFilter.isEmpty() && !stationStreetNumberFilter.matches("^-?\\d+(\\.\\d+)?$")){
            DialogUtils.wrongInputAlert(stationStreetNumberFilter,streetNumberLabel.getText());
        } else if (!stationPostalCodeFilter.isEmpty() && !stationPostalCodeFilter.matches("^-?[\\d-]+$")) {
            DialogUtils.wrongInputAlert(stationPostalCodeFilter,postalCodeLabel.getText());
        }else{
            List<Station> stationsFiltered= stations.stream().
                    filter(train -> train.getStationName().toLowerCase().contains(stationNameFilter.toLowerCase())).
                    filter(train -> train.getAddress().cityName().toLowerCase().contains(stationCityFilter.toLowerCase())).
                    filter(station ->  station.getAddress().streetNumber().toLowerCase().contains(stationStreetNumberFilter.toLowerCase())).
                    filter(station ->  station.getAddress().postalCode().toLowerCase().contains(stationPostalCodeFilter.toLowerCase())).
                    filter(station -> station.getAddress().streetName().toLowerCase().contains(stationStreetNameFilter.toLowerCase())).
                    toList();
            ObservableList<Station> stationObservableList = FXCollections.observableArrayList(stationsFiltered);
            stationTableView.setItems(stationObservableList);
        }
    }
    public void backupStationsTable(){
        Thread.startVirtualThread(DatabaseManager::backupStationsTable);
    }

    /**
     * Opens SelectedStationController that allows modification of the selected Station
     * @param selectedStation is the Station selected by double clicking the entry
     */
    public void openSelectedStation(Station selectedStation){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("selected-station.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            SelectedStationController controller = fxmlLoader.getController();
            controller.setSelectedStation(selectedStation);
            TrainAdministrationApplication.getMainStage().setTitle("Train management");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _) {
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error while selecting an train");
        }

    }
}
