package hr.javafx.data;

import hr.javafx.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserChangeLogger {
    public static final String USER_CHANGE_LOG ="Data/UserLogs/UserActions.bin";
    private static final Logger log = LoggerFactory.getLogger(UserChangeLogger.class);
    private UserChangeLogger(){
    }

    /**
     * is used to make a log about a created object
     * @param user is the user that created the object
     * @param createdObject is the object that is being created
     * @param <T> is used because both Train and Station can be created
     */
    public static <T extends Serializable> void logCreate(User user, T createdObject){
        UserLogEntry<T> log=new UserLogEntry<>(user,"Created",null,createdObject);
        logUserAction(log);
    }

    /**
     * is used to make a log about a deleted object
     * @param user is the user that deleted the object
     * @param deletedObject is the object that is being deleted
     * @param <T> is used because both Train and Station can be deleted
     */
    public static <T extends Serializable> void logDelete(User user, T deletedObject){
        UserLogEntry<T> log=new UserLogEntry<>(user,"Deleted",deletedObject,null);
        logUserAction(log);
    }

    /**
     * is used to make a log about an updated object
     * @param user is the user that made an update
     * @param objectBeforeChange is the object before the change
     * @param objectAfterChange is the object after the change
     * @param <T> is used because both Train and Station can be updated
     */
    public static <T extends Serializable> void logChanged(User user, T objectBeforeChange, T objectAfterChange){
        UserLogEntry<T> log=new UserLogEntry<>(user,"Created",objectBeforeChange,objectAfterChange);
        logUserAction(log);
    }

    /**
     * adds a new log to the end of the List of all logs and saves it inside the USER_CHANGE_LOG file
     * @param entry is the log that is being appended to the list
     * @param <T> is being used because the object being logged can be either Train or Station
     */
    private static <T extends Serializable> void logUserAction(UserLogEntry<T> entry) {
        try {
            List<UserLogEntry<?>> logs = readAllLogs();
            logs.add(entry);

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(USER_CHANGE_LOG))) {
                oos.writeObject(logs);
            }

            log.info("Action logged: {} by {}", entry.getActionType(), entry.getUser().getUsername());

        } catch (IOException e) {
            log.error("Failed to write audit log", e);
        }
    }

    /**
     * reads the List that was saved as an object and retrieves it
     * @return returns the List of logs
     */
    @SuppressWarnings({"unchecked", "java:S1452"})
    public static List<UserLogEntry<? extends Serializable>> readAllLogs() {
        File file = new File(USER_CHANGE_LOG);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(USER_CHANGE_LOG))) {
            return (List<UserLogEntry<?>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read audit log", e);
            return new ArrayList<>();
        }
    }
}
