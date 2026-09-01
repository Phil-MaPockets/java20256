package hr.javafx.controllers;

import hr.javafx.data.UserChangeLogger;
import hr.javafx.data.UserLogEntry;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Objects;

public class LogController {
    @FXML
    TableView<UserLogEntry<?>> userLogEntryTableView;
    @FXML
    TableColumn<UserLogEntry<?>,String> userColumn;
    @FXML
    TableColumn<UserLogEntry<?>,String> timestamp;
    @FXML
    TableColumn<UserLogEntry<?>,String> actionType;
    @FXML
    TableColumn<UserLogEntry<?>,String> beforeChange;
    @FXML
    TableColumn<UserLogEntry<?>,String> afterChange;

    List<UserLogEntry<?>> logs = UserChangeLogger.readAllLogs();

    public void initialize(){
        ObservableList<UserLogEntry<?>> observableLogs= FXCollections.observableList(logs);
        userLogEntryTableView.setItems(observableLogs);
        userColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getUser().getName()));
        timestamp.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getTimestamp().toString()));
        actionType.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getActionType()));
        beforeChange.setCellValueFactory(param->new ReadOnlyStringWrapper(Objects.toString(param.getValue().getObjectBefore(),"-")));
        afterChange.setCellValueFactory(param->new ReadOnlyStringWrapper(Objects.toString(param.getValue().getObjectAfter(),"-")));
    }
}
