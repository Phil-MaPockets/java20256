package hr.javafx.controllers;

import hr.javafx.data.DatabaseManager;
import hr.javafx.main.TrainAdministrationApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MainController {
    @FXML
    private Label infoLabel;
    @FXML
    private Button stationInfoButton;
    @FXML
    private Button trainInfoButton;
    @FXML
    private Label currentUser;
    private Runnable currentAction;

    public void initialize(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            if (currentAction != null) {
                currentAction.run();
            }
        }));

        trainInfoButton.setOnAction(e -> currentAction = this::getLastTrain);
        stationInfoButton.setOnAction(e -> currentAction = this::getLastStation);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        currentUser.setText("Bok, " + TrainAdministrationApplication.getCurrentUser().getName() + " " + TrainAdministrationApplication.getCurrentUser().getSurname());
    }

    /**
     * Finds the Last inserted Train in the Trains table
     */
    private void getLastTrain(){
        Thread.startVirtualThread(() -> {
            String result = DatabaseManager.getLatestTrain();
            Platform.runLater(() -> infoLabel.setText(result));
        });
    }

    /**
     * Finds the Last inserted Station in the Stations table
     */
    private void getLastStation(){
        Thread.startVirtualThread(() -> {
            String result = DatabaseManager.getLatestStation();
            Platform.runLater(() -> infoLabel.setText(result));
        });
    }
}
