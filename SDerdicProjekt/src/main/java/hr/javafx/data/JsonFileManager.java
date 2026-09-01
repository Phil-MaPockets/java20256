package hr.javafx.data;

import hr.javafx.model.User;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonFileManager {
    private static final Logger log = LoggerFactory.getLogger(JsonFileManager.class);
    private static final JsonbConfig config=new JsonbConfig().withFormatting(true).withNullValues(true).withEncoding("UTF-8");
    private static final Jsonb jsonb = JsonbBuilder.create(config);

    private JsonFileManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * saves data that is passed on into a .Json file
     * @param filePath is the location of the .Json file in which the data is being saved
     * @param data is the data being saved
     * @param <T> technically legacy because i forgot to change it to User, all .Json files were being used before
     */
    public static <T> void saveToJson(String filePath, List<T> data) {
        try (Writer writer = new FileWriter("data/"+filePath)) {
            jsonb.toJson(data, writer);
            log.info("Data saved to JSON file: {}", filePath);
        } catch (IOException e) {
            log.error("Error saving to JSON file: {}", filePath, e);
        }
    }

    /**
     * returns all data from the .Json file as a string
     * @param filePath is the path to the .Json file which is being read
     * @return is the String value of the .Json file
     */
    public static String readFromJson(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", filePath, e);
        }
        return null;
    }

    /**
     * creates a list of objects being read
     * @param filePath is the path to the .Json file which is used to create the List
     * @param type is the name of the Class that we are casting into
     * @return is the List of data created
     * @param <T> technically legacy because i forgot to change it to User, all .Json files were being used before
     */
    public static <T> List<T> createObjects(String filePath, Type type) {
        try {
            return jsonb.fromJson(readFromJson("data/"+filePath), type);
        }catch (RuntimeException e){
            log.error("Error creating tickets from JSON file: {}", e.getMessage());
        }
        return List.of();
    }

    /**
     * adds Tickets to a user that made a Reservation
     * @param filePath is the path to the .Json file in which the data is being updated
     * @param updatedUser is the user that is being updated
     * @param users is the List of all users to sort trough
     */
    public static void updateUserTickets(String filePath, User updatedUser, List<User> users) {
        List<User> updatedUsers = users.stream()
                .map(user -> user.getId() == updatedUser.getId() ? updatedUser : user)
                .toList();
        saveToJson(filePath, updatedUsers);
    }
}
