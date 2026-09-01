package hr.javafx.controllers;

import hr.javafx.data.JsonFileManager;
import hr.javafx.model.User;
import hr.javafx.model.UserRole;
import hr.javafx.util.DialogUtils;
import hr.javafx.util.MethodUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserInputController {
    private static final Logger log = LoggerFactory.getLogger(UserInputController.class);
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField userSurnameInput;
    @FXML
    private TextField userUsernameInput;
    @FXML
    private PasswordField userPasswordInput;
    @FXML
    private PasswordField userPasswordConfirmation;
    @FXML
    private ComboBox<UserRole> userRoleInput;


    List<User> users=JsonFileManager.createObjects("Users.json",new ArrayList<User>(){}.getClass().getGenericSuperclass());
    public void initialize(){
        userRoleInput.setItems(MethodUtils.setUserRole());
    }

    /**
     * Adds the user to Users.Json
     */
    public void addUser(){
        List<String> emptyFields=new ArrayList<>();
        String name=userNameInput.getText();
        String surname=userSurnameInput.getText();
        String username= userUsernameInput.getText();
        String password=userPasswordInput.getText();
        String passwordConfirm=userPasswordConfirmation.getText();
        UserRole role=userRoleInput.getValue();

        if (name == null || name.trim().isEmpty()) emptyFields.add("Name");
        if (surname == null || surname.trim().isEmpty()) emptyFields.add("Surname");
        if (username == null || username.trim().isEmpty()) emptyFields.add("Username");
        if (password == null || password.trim().isEmpty()) emptyFields.add("Password");
        if (passwordConfirm == null || passwordConfirm.trim().isEmpty()) emptyFields.add("Password Confirm");
        if (role==null) emptyFields.add("Role");

        if(!emptyFields.isEmpty()){
            DialogUtils.missingInputAlert(emptyFields);
            log.info("Adding a new user failed because some fields remain empty");
        } else if (!Objects.equals(password, passwordConfirm)) {
            DialogUtils.passwordMismatchAlert();
            log.info("Adding a new user failed because the Passwords did not match");
        }
        else{
            String hashPassword=MethodUtils.hashPassword(password);
            User newUser=new User(name,surname,hashPassword,username,role);
            users.add(newUser);
            JsonFileManager.saveToJson("Users.json",users);
        }
    }
}
