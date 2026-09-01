package hr.javafx.controllers;

import hr.javafx.data.JsonFileManager;
import hr.javafx.main.TrainAdministrationApplication;
import hr.javafx.model.User;
import hr.javafx.util.DialogUtils;
import hr.javafx.util.MethodUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogInController {
    private static final Logger log = LoggerFactory.getLogger(LogInController.class);
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    List<User> users=JsonFileManager.createObjects("Users.json",new ArrayList<User>(){}.getClass().getGenericSuperclass());

    /**
     * Performs a check of the username and password to check if there is a match in Users.Json
     */
    public void logIn(){
        String username= usernameInput.getText();
        String password= passwordInput.getText();
        int counter=0;
        for(User user : users){
            counter++;
            if(user.getUsername().equals(username)) {
                boolean passwordMatch = MethodUtils.verifyPassword(password, user.getPassword());
                if(passwordMatch) {
                    TrainAdministrationApplication.setCurrentUser(user);
                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("main-display.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
                        TrainAdministrationApplication.getMainStage().setTitle("Station Overview");
                        TrainAdministrationApplication.getMainStage().setScene(scene);
                        TrainAdministrationApplication.getMainStage().show();
                        return;
                    } catch (IOException e) {
                        DialogUtils.showDisplayingScreenError();
                        log.error("Error loading main menu", e);
                    }
                }
            }
        }
        if(counter== users.size()){
            DialogUtils.wrongLoginInfo();
        }
    }


}
