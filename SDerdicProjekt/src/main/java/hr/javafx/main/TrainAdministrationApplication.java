package hr.javafx.main;

import hr.javafx.model.User;
import hr.javafx.model.UserRole;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TrainAdministrationApplication extends Application {
    private static TrainAdministrationApplication instance;
    private Stage mainStage;
    private User currentUser;

    public TrainAdministrationApplication() {
        instance = this;
    }

    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(TrainAdministrationApplication.class.getResource("log-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getMainStage() {
        return instance != null ? instance.mainStage : null;
    }

    public static void setCurrentUser(User loggedInUser) {
        if (instance != null) {
            instance.currentUser = loggedInUser;
        }
    }

    public static User getCurrentUser() {
        return instance != null ? instance.currentUser : null;
    }

    public static boolean isAdmin() {
        return instance != null &&
                instance.currentUser != null &&
                instance.currentUser.getRole() == UserRole.ADMIN;
    }

}
