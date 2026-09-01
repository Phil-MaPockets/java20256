package hr.javafx.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;

public class DialogUtils {
    private DialogUtils() {
    }

    /**
     * displays that an error occured when trying to open a new screen
     */
    public static void showDisplayingScreenError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error displaying screen");
        alert.setHeaderText("The program has failed to display the chosen screen");
        alert.setContentText("There was an error with the initialisation of the screen");
        alert.showAndWait();
    }

    /**
     * warns the user that an inapropriate type of data was inserted into an input field
     * @param s is the text that the user wrote
     * @param label is the field in which the user wrote
     */
    public static void wrongInputAlert(String s,String label){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong data type");
        alert.setHeaderText("You have typed in '" + s + "' in the " + label + " field");
        alert.setContentText("Please type a number into the "+label+" field");
        alert.showAndWait();
    }

    /**
     * warns the user that a number of fields is empty and should be filled
     * @param emptyFields is the list of all the fields that need to be filled
     */
    public static void missingInputAlert(List<String> emptyFields){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Empty fields");
        alert.setHeaderText("Please fill in the following fields:");
        alert.setContentText(String.join("\n-",emptyFields));
        alert.showAndWait();
    }

    /**
     * warns the user that they mistyped the Confirm Password field
     */
    public static void passwordMismatchAlert(){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Password Mismatch");
        alert.setContentText("Passwords in the Password and Confirm Password fields don't match");
        alert.showAndWait();
    }

    /**
     * warns the user that an object with the same name already exists
     * @param className is the name of the object
     */
    public static void duplicateInput(String className){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Duplicate Data");
        alert.setContentText("An " + className + " with the same name already exists in the database");
        alert.showAndWait();
    }

    /**
     * warns the user that they inputed the wrong username or password
     */
    public static void wrongLoginInfo(){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong username or password");
        alert.showAndWait();
    }

    /**
     * warns the user that the train cannot be deleted because it still has reserved tickets
     */
    public static void cannotDeleteTrain(){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cannot delete selected Train");
        alert.setHeaderText("The selected train still has Reserved tickets");
        alert.showAndWait();
    }

    /**
     * warns the user that the station cannot be deleted because it still has trains
     */
    public static void cannotDeleteStation(){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cannot delete selected Station");
        alert.setHeaderText("The selected station has trains");
        alert.setContentText("Please move all the trains to different stations before deleting this station");
        alert.showAndWait();
    }

    /**
     * warns the user that the ticket is already reserved by another user
     * @param user the user that reserved the ticket
     * @param ticketID the ID of the chosen ticket
     */
    public static void ticketAlreadyReserved(String user, int ticketID){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ticket is already reserved");
            alert.setHeaderText("Ticket " + ticketID + " is reserved by the user: " + user);
            alert.showAndWait();
        });
    }

    /**
     * warns the user that they need to select a ticket to reserve
     */
    public static void noTicketSelected(){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Ticket is selected");
        alert.setHeaderText("please select a ticket from the Table below");
        alert.showAndWait();
    }
}
