package hr.javafx.controllers;

import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.util.DialogUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MenuController {
    private static final Logger log= LoggerFactory.getLogger(MenuController.class);

    /**
     * Opens the StationController when selecting the menuItem Display under the menu Stations
     */
    public void showStations(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("station-display.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            TrainAdministrationApplication.getMainStage().setTitle("Station Overview");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _) {
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error in the connection between StationController and station-display.fxml");
        }
    }
    /**
     * Opens the StationInputController when selecting the menuItem Insert under the menu Stations
     */
    public void showStationInput(){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(TrainAdministrationApplication.class.getResource("station-input-display.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),1024,768);
            TrainAdministrationApplication.getMainStage().setTitle("Station Management");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _){
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error in the connection between StationInputController and station-input-display.fxml");
        }
    }
    /**
     * Opens the TrainController when selecting the menuItem Display under the menu Trains
     */
    public void showTrains(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("train-display.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            TrainAdministrationApplication.getMainStage().setTitle("Train Overview");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _) {
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error in the connection between TrainController and train-display.fxml");
        }
    }
    /**
     * Opens the TrainInputController when selecting the menuItem Insert under the menu Train
     */
    public void showTrainInput(){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(TrainAdministrationApplication.class.getResource("train-input-display.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),1024,768);
            TrainAdministrationApplication.getMainStage().setTitle("Train Management");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _){
            DialogUtils.showDisplayingScreenError();
            log.error("There was an error in the connection between TrainInputController and train-input-display.fxml");
        }
    }
    /**
     * Opens the UserInputController when selecting the menuItem Add User under the menu User
     */
    public void showUsers(){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(TrainAdministrationApplication.class.getResource("user-input-display.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),1024,768);
            TrainAdministrationApplication.getMainStage().setTitle("User Management");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _){
            DialogUtils.showDisplayingScreenError();
        }
    }
    /**
     * Opens the LogInController when selecting the menuItem Log Out under the menu User
     */
    public void logOut(){
        try{
            TrainAdministrationApplication.setCurrentUser(null);
            FXMLLoader fxmlLoader=new FXMLLoader(TrainAdministrationApplication.class.getResource("log-in.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),1024,768);
            TrainAdministrationApplication.getMainStage().setTitle("Log in");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _){
            DialogUtils.showDisplayingScreenError();
        }
    }

    /**
     * Opens the LogController when selecting menuItem Logs under the menu User
     */
    public void openLogs(){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(TrainAdministrationApplication.class.getResource("logs.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),1024,768);
            TrainAdministrationApplication.getMainStage().setTitle("Logs");
            TrainAdministrationApplication.getMainStage().setScene(scene);
            TrainAdministrationApplication.getMainStage().show();
        } catch (IOException _){
            DialogUtils.showDisplayingScreenError();
        }
    }
}
