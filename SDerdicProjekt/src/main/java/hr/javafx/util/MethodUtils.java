package hr.javafx.util;

import hr.javafx.model.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class MethodUtils {
    private MethodUtils(){}
    private static final int COST=12;

    /**
     * is used to fill a comboBox with Time increments of 15 minutes
     * @return gives an ObservableList that is set into a ComboBox
     */
    public static ObservableList<LocalTime> setComboBoxTime(){
        LocalTime time = LocalTime.MIDNIGHT;
        List<LocalTime> timeSet=new ArrayList<>();
        for (int i = 0; i < 96; i++) {
            timeSet.add(time);
            time = time.plusMinutes(15);
        }
        return FXCollections.observableArrayList(timeSet);
    }

    /**
     * is used to fill a comboBox with all existing user Roles
     * @return gives an ObservableList that is set into a comboBox
     */
    public static ObservableList<UserRole> setUserRole(){
        List<UserRole> roles=Arrays.asList(UserRole.values());
        return FXCollections.observableArrayList(roles);
    }

    /**
     * hashes the password with BCrypt hashing
     * @param password is the password that is being hashed
     * @return gives the hashed password
     */
    public static String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt(COST));
    }

    /**
     * compares the password the user wrote with the hashed password inside the Users.Json file
     * @param inputPassword is the users password
     * @param hashPassword is the hashed password inside the .Json file
     * @return gives feedback if the password is correct
     */
    public static boolean verifyPassword(String inputPassword, String hashPassword){
        return BCrypt.checkpw(inputPassword,hashPassword);
    }

}
