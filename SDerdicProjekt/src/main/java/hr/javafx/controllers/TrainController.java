package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.Station;
import hr.javafx.model.Train;
import hr.javafx.util.DialogUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static hr.javafx.util.MethodUtils.setComboBoxTime;

public class TrainController {
    private static final Logger log = LoggerFactory.getLogger(TrainController.class);
    @FXML
    private TextField stationTextField;
    @FXML
    private TextField trainNameField;
    @FXML
    private DatePicker trainDepartureDate;
    @FXML
    private ComboBox<LocalTime> trainDepartureTime;
    @FXML
    private DatePicker trainArrivalDate;
    @FXML
    private ComboBox<LocalTime> trainArrivalTime;
    @FXML
    private TableView<Train> trainTableView;
    @FXML
    private TableColumn<Train, String> trainIDColumn;
    @FXML
    private TableColumn<Train, String> trainNameColumn;
    @FXML
    private TableColumn<Train, String> trainStationColumn;
    @FXML
    private TableColumn<Train, String> trainDepartureDateTime;
    @FXML
    private TableColumn<Train, String> trainArrivalDateTime;

    List<Station> stations = DatabaseManager.getStations();
    List<Train> trains = DatabaseManager.getTrains();

    public void initialize(){
        ObservableList<Train> trainObservableList = FXCollections.observableArrayList(trains);
        trainDepartureTime.setItems(setComboBoxTime());
        trainArrivalTime.setItems(setComboBoxTime());
        trainTableView.setItems(trainObservableList);
        trainIDColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId())));
        trainNameColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getTrainName()));
        trainStationColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(stations.get(param.getValue().getStationID()-1).getStationName()));
        trainDepartureDateTime.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getDepartureTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        trainArrivalDateTime.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getArrivalTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        trainTableView.setRowFactory(view->{TableRow<Train> row=new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2&&!row.isEmpty()&& TrainAdministrationApplication.isAdmin()){
                    Train selectedTrain =row.getItem();
                    openSelectedTrain(selectedTrain);
                } else if (mouseEvent.getClickCount()==2&&!row.isEmpty()&&!TrainAdministrationApplication.isAdmin()) {
                    Train selectedTrain =row.getItem();
                    openTrainReservation(selectedTrain);
                }else if (mouseEvent.getButton()== MouseButton.SECONDARY&&!row.isEmpty()&& TrainAdministrationApplication.isAdmin()){
                    Train selectedTrain =row.getItem();
                    openTrainReservation(selectedTrain);
                }
            });
            return row;
        });
    }

    /**
     * Searches through all the trains based of the criteria written in the fields above
     */
    public void searchTrains(){
        String trainNameFilter=trainNameField.getText();
        String stationFilter=stationTextField.getText();
        LocalDate selectedTrainDepartureDate = trainDepartureDate.getValue();
        LocalTime selectedTrainDepartureTime=trainDepartureTime.getValue();
        LocalDate selectedTrainArrivalDate=trainArrivalDate.getValue();
        LocalTime selectedTrainArrivalTime=trainArrivalTime.getValue();
        List<Train> trainsFiltered= trains.stream().
                filter(train -> train.getTrainName().toLowerCase().contains(trainNameFilter.toLowerCase())).
                filter(train -> stations.get(train.getStationID()-1).getStationName().toLowerCase().contains(stationFilter.toLowerCase())).
                filter(train -> {
                    if (selectedTrainDepartureDate == null) return true;
                    return train.getDepartureTime().toLocalDate().equals(selectedTrainDepartureDate);
                }).
                filter(train ->{
                    if (selectedTrainDepartureTime == null) return true;
                    return train.getDepartureTime().toLocalTime().equals(selectedTrainDepartureTime);
                }).filter(train -> {
                    if (selectedTrainArrivalDate == null) return true;
                    return train.getArrivalTime().toLocalDate().equals(selectedTrainArrivalDate);
                }).
                filter(train ->{
                    if (selectedTrainArrivalTime == null) return true;
                    return train.getArrivalTime().toLocalTime().equals(selectedTrainArrivalTime);
                }).
                toList();
        ObservableList<Train> trainFilteredObservableList = FXCollections.observableArrayList(trainsFiltered);
        trainTableView.setItems(trainFilteredObservableList);
    }
    public void backupTrainTable(){
        Thread.startVirtualThread(DatabaseManager::backupTrainsTable);
    }

    /**
     * Opens the SelectedTrainController, allowing modification of the selected train
     * @param train is the train selected by double clicking while the user is an Admin
     */
    public void openSelectedTrain(Train train){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("selected-train.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            SelectedTrainController controller = fxmlLoader.getController();
            controller.setSelectedTrain(train);
            TrainAdministrationApplication.getMainStage().setTitle("Train management");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _) {
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error while selecting an train");
        }
    }

    /**
     * Opens the TicketReservationController, allowing reservation of tickets for Admin and User roles, Admin needs to right click while the user needs to double click an entry in TableView
     * @param train is the train selected by double clicking or right clicking
     */
    public void openTrainReservation(Train train){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("ticket-reservation.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            TicketReservationController controller = fxmlLoader.getController();
            controller.setSelectedTrain(train);
            TrainAdministrationApplication.getMainStage().setTitle("Ticket Reservation");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _) {
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error while selecting an train");
        }
    }

}
