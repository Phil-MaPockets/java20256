package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.data.JsonFileManager;
import hr.javafx.exceptions.SeatAlreadyReservedException;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.*;
import hr.javafx.util.DialogUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class TicketReservationController {
    private static final Logger log = LoggerFactory.getLogger(TicketReservationController.class);
    @FXML
    private Label trainStation;
    @FXML
    private Label trainName;
    @FXML
    private Label trainDepartureDateTime;
    @FXML
    private Label trainArrivalDateTime;
    @FXML
    private TableView<Ticket> ticketTableView;
    @FXML
    private TableColumn<Ticket, String> ticketClass;
    @FXML
    private TableColumn<Ticket, String> ticketPrice;
    @FXML
    private TableColumn<Ticket, String> ticketSeat;
    @FXML
    private TableColumn<Ticket,String> ticketStatus;
    Train selectedTrain;
    private Ticket selectedTicket;
    List<Station> stations = new ArrayList<>(DatabaseManager.getStations());
    List<User> users= JsonFileManager.createObjects("Users.json",new ArrayList<User>(){}.getClass().getGenericSuperclass());
    List<Reservation<User, Ticket>> reservations = new ArrayList<>(users.stream()
            .flatMap(user -> user.getTickets().stream()
                    .map(ticket -> new Reservation<>(user, ticket)))
            .toList());

    /**
     * Is Used as a method to pass on the Train that was selected on the TrainController screen
     * @param train is the Train whose info loads into the fields on the screen and whose tickets load into the TableView
     */
    public void setSelectedTrain(Train train){
        selectedTrain = train;
        trainStation.setText(stations.get(selectedTrain.getStationID()).getStationName());
        trainName.setText(selectedTrain.getTrainName());
        trainDepartureDateTime.setText(selectedTrain.getDepartureTime().toString());
        trainArrivalDateTime.setText(selectedTrain.getArrivalTime().toString());

        ObservableList<Ticket> tickets=FXCollections.observableArrayList(selectedTrain.getTickets());
        ticketTableView.setItems(tickets);
        ticketClass.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getPrice().toString()));
        ticketPrice.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getPrice().toString()));
        ticketSeat.setCellValueFactory(param->new ReadOnlyStringWrapper(String.valueOf(param.getValue().getSeatNumber())));
        ticketStatus.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().isReserved().equals(SeatStatus.RESERVED) ? "RESERVED" : "UNRESERVED"));
        ticketTableView.getSelectionModel().selectedItemProperty().addListener(
                (_, _, newValue) -> {
                    if (newValue != null) {
                        selectedTicket = newValue;
                    }
                }
        );
    }

    /**
     * Reserves the Ticket selected in the TableView when pressing the button Reserve Selected Ticket
     */
    public void reserveTicket(){
        String reservedBy = reservations.stream()
                .filter(reservation -> reservation.getTicket().getId() == selectedTicket.getId())
                .map(Reservation::getUser)
                .map(User::getName)
                .findFirst()
                .orElse("Unknown");
        Thread.startVirtualThread(()->{
            try{
                if(selectedTicket.isReserved().equals(SeatStatus.RESERVED)) {
                    throw new SeatAlreadyReservedException(String.format("Ticket %d is reserved by %s", selectedTicket.getId(), reservedBy));
                } else if (selectedTicket==null) {
                    Platform.runLater(DialogUtils::noTicketSelected);
                }else{
                    selectedTicket.setReservation();
                    User currentUser= TrainAdministrationApplication.getCurrentUser();
                    Reservation<User,Ticket> reservation=new Reservation<>(currentUser,selectedTicket);
                    assert currentUser != null;
                    currentUser.addTicket(selectedTicket);
                    reservations.add(reservation);
                    JsonFileManager.updateUserTickets("Users.json",currentUser,users);
                    DatabaseManager.reserveTicket(selectedTicket);
                    log.info("Ticket {} reserved by {}",selectedTicket.getId(),currentUser.getName());

                    Platform.runLater(()->ticketTableView.refresh());
                }
            }catch (SeatAlreadyReservedException e){
                log.warn(e.getMessage());
                DialogUtils.ticketAlreadyReserved(reservedBy, selectedTicket.getId());
            }
        });
    }
}
