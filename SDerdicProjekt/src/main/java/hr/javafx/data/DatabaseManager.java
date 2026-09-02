package hr.javafx.data;
import hr.javafx.exceptions.DuplicateTrainException;
import hr.javafx.exceptions.DuplicateStationException;
import hr.javafx.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class DatabaseManager{
    private DatabaseManager(){}
    private static final Logger log= LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DATABASE_FILE = "database/Database.properties";

    /**
     * Makes a connection to the database so that SQL queries in other methods can be performed
     * @return passes on the Connection object to make a query
     * @throws SQLException in the case of an SQL error
     * @throws IOException in the case the DATABASE_FILE cannot be found
     */
    private static Connection connectToDatabase() throws SQLException, IOException{
        try( var reader=new FileReader(DATABASE_FILE)) {
            var properties = new Properties();
            properties.load(reader);
            var url=properties.getProperty("Url");
            var user=properties.getProperty("Username");
            var pass=properties.getProperty("Password");
            return DriverManager.getConnection(url,user,pass);}
    }

    /**
     * Is used to retrieve all Tickets from the TICKETS table
     * @return passes on the List of tickets in the TICKETS table
     */
    public static List<Ticket> getTickets(){
        List<Ticket> tickets = new ArrayList<>();
        try (Connection db = connectToDatabase(); PreparedStatement query = db.prepareStatement("SELECT * FROM TICKETS"); ResultSet rs = query.executeQuery()) {
            while (rs.next()) {
                Ticket ticket = switch (rs.getString("class")) {
                    case "Business" -> new BusinessClass(rs.getInt("id"), rs.getInt("seat"), rs.getInt(3), rs.getBigDecimal(5));
                    case "Economy" -> new Economy(rs.getInt("id"), rs.getInt("seat"), rs.getInt(3), rs.getBigDecimal(5));
                    default -> new FirstClass(rs.getInt("id"), rs.getInt("seat"), rs.getInt(3), rs.getBigDecimal(5));
                };
                if ("RESERVED".equals(rs.getString("status"))) ticket.setReservation();
                tickets.add(ticket);
            }
        } catch (SQLException | IOException e) {
            log.error("Error reading the Tickets table", e);}
        return tickets;
    }

    /**
     * is used to retrieve all Trains from the TRAINS table
     * @return passes on the List of trains in the TRAINS table
     */
    public static List<Train> getTrains(){
        List<Train> trains = new ArrayList<>();
        List<Ticket> tickets = getTickets();
        try (Connection db = connectToDatabase(); PreparedStatement query = db.prepareStatement("SELECT * FROM TRAINS"); ResultSet rs = query.executeQuery()) {
            while(rs.next()){
                int id = rs.getInt("id");
                Train train = new Train.Builder().id(id).stationID(rs.getInt("station")).trainName(rs.getString("name")).build();
                train.setDepartureTime(rs.getTimestamp("departure").toLocalDateTime());
                train.setArrivalTime(rs.getTimestamp("arrival").toLocalDateTime());
                train.setTickets(tickets.stream().filter(t -> t.getTrainID() == id).toList());
                trains.add(train);
            }
        } catch (SQLException | IOException e) {
            log.error("Error reading the Trains table", e);}
        return trains;
    }
    /**
     * is used to retrieve all stations from the STATIONS table
     * @return passes on the List of stations in the STATIONS table
     */
    public static List<Station> getStations(){
        List<Station> stations = new ArrayList<>();
        List<Train> trains = getTrains();
        try (Connection db = connectToDatabase(); PreparedStatement query = db.prepareStatement("SELECT * FROM STATIONS"); ResultSet rs = query.executeQuery()) {
            while(rs.next()){
                int id = rs.getInt("id");
                Address address = new Address(rs.getString("street_name"), rs.getString("street_number"), rs.getString("postal_code"), rs.getString("city_name"));
                Station station = new Station(id, rs.getString("name"), address);
                station.setTrains(trains.stream().filter(plane -> plane.getStationID() == id).toList());
                stations.add(station);
            }
        } catch (SQLException | IOException e) {
            log.error("Error reading the Stations table", e);}
        return stations;
    }

    /**
     * checks if a Station with the same name already exists in the table STATIONS
     * @param station is the station that we are checking
     * @return gives the information if a duplicate exists
     * @throws SQLException in the case of an SQL error
     * @throws IOException in the case that DATABASE_PROPERTIES cannot be found
     */
    public static boolean checkStationDuplicates(Station station) throws SQLException,IOException{
        try (Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("SELECT COUNT(*) FROM STATIONS WHERE name = ?")){
            query.setString(1, station.getStationName());
            ResultSet rs= query.executeQuery();
            if(rs.next()) return rs.getInt(1)>0;}
        return false;
    }
    /**
     * checks if a Train with the same name already exists in the table TRAINS
     * @param train is the station that we are checking
     * @return gives the information if a duplicate exists
     * @throws SQLException in the case of an SQL error
     * @throws IOException in the case that DATABASE_PROPERTIES cannot be found
     */
    public static boolean checkTrainDuplicates(Train train) throws SQLException,IOException{
        try (Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("SELECT COUNT(*) FROM TRAINS WHERE name = ?")){
            query.setString(1, train.getTrainName());
            ResultSet rs= query.executeQuery();
            if(rs.next()) return rs.getInt(1)>0;}
        return false;
    }

    /**
     * inserts the station into the table STATIONS
     * @param station is the Station that are being added
     * @throws DuplicateStationException in the case the Station already exists
     * @throws SQLException in the case of an SQL error
     * @throws IOException in the case that DATABASE_PROPERTIES cannot be found
     */
    public static void insertStation(Station station) throws DuplicateStationException,SQLException,IOException{
        if(checkStationDuplicates(station)) throw new DuplicateStationException(station.getStationName());
        try (Connection db = connectToDatabase();PreparedStatement query=db.prepareStatement("INSERT INTO STATIONS (name, street_name, street_number, postal_code, city_name) VALUES (?, ?, ?, ?, ?)")){
            query.setString(1, station.getStationName());
            query.setString(2, station.getAddress().streetName());
            query.setString(3, station.getAddress().streetNumber());
            query.setString(4, station.getAddress().postalCode());
            query.setString(5, station.getAddress().cityName());
            query.executeUpdate();}
    }
    /**
     * inserts the station into the table STATIONS
     * @param train is the Train that are being added
     * @throws DuplicateTrainException in the case the Train already exists
     * @throws SQLException in the case of an SQL error
     * @throws IOException in the case that DATABASE_PROPERTIES cannot be found
     */
    public static void insertTrain(Train train) throws DuplicateTrainException, SQLException, IOException{
        if(checkTrainDuplicates(train)) throw new DuplicateTrainException(train.getTrainName());
        try (Connection db = connectToDatabase();PreparedStatement query=db.prepareStatement("INSERT INTO TRAINS (station, name, departure, arrival) VALUES (?, ?, ?, ?)")){
            query.setInt(1, train.getStationID()); // stationID = station.id
            query.setString(2, train.getTrainName());
            query.setTimestamp(3, Timestamp.valueOf(train.getDepartureTime()));
            query.setTimestamp(4, Timestamp.valueOf(train.getArrivalTime()));
            query.executeUpdate();}
    }

    /**
     * inserts the automatically generated tickets for the Train that is being inserted
     * @param tickets is the List of tickets that are being added
     */
    public static void insertTickets(List<Ticket> tickets) {
        String sql = "INSERT INTO TICKETS (SEAT, TRAIN, STATUS, PRICE, CLASS) VALUES (?,?,?,?,?)";
        try (Connection db = connectToDatabase();
             PreparedStatement query = db.prepareStatement(sql)) {
            for (Ticket ticket : tickets) {
                query.setInt(1, ticket.getSeatNumber());
                query.setInt(2, ticket.getTrainID());
                query.setString(3, ticket.isReserved().name());
                query.setFloat(4, ticket.getPrice().floatValue());
                query.setString(5, ticket.getClassName().equals("BusinessClass") ? "Business" : ticket.getClassName());
                query.addBatch();
            }
            query.executeBatch();
        } catch (SQLException | IOException e) {
            log.error("Error inserting tickets", e);}
    }
    /**
     * creates a table TRAINS_BACKUP with all the information currently inside the table TRAINS
     */
    public static void backupTrainsTable(){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("DROP TABLE IF EXISTS TRAINS_BACKUP; CREATE TABLE TRAINS_BACKUP AS SELECT * FROM TRAINS;")){
            query.executeUpdate();

        } catch(SQLException|IOException e){
            log.error("Error backing up the database", e);}
    }

    /**
     * creates a table STATIONS_BACKUP with all the information currently inside the table STATIONS
     */
    public static void backupStationsTable(){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("DROP TABLE IF EXISTS STATIONS_BACKUP; CREATE TABLE STATIONS_BACKUP AS SELECT * FROM STATIONS;")){
            query.executeUpdate();
        } catch(SQLException|IOException e){
            log.error("Error backing up the database", e);}
    }

    /**
     * retrieves that last Train added to the TRAINS table
     * @return returns the ID and Name of the last added TRAIN
     */
    public static String getLatestTrain(){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("SELECT * FROM TRAINS ORDER BY ID DESC LIMIT 1")){
            ResultSet rs=query.executeQuery();
            if(rs.next()) return "ID: " + rs.getInt(1)+" Name: "+rs.getString(3);
        } catch(SQLException|IOException e){
            log.error("Error fetching last item", e);}
        return "Error";
    }

    /**
     * retrieves the last Station added to the STATIONS table
     * @return returns the ID and name of the last added STATION
     */
    public static String getLatestStation(){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("SELECT * FROM STATIONS ORDER BY ID DESC LIMIT 1")){
            ResultSet rs=query.executeQuery();
            if(rs.next()) return "ID: " + rs.getInt(1)+" Name: "+rs.getString(2);
        } catch(SQLException|IOException e){
            log.error("Error fetching last item", e);}
        return "Error";
    }

    /**
     * deletes a Train from the table TRAINS
     * @param train is the Train that will be deleted
     */
    public static void deleteTrain(Train train){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("DELETE FROM TRAINS WHERE ID = ?")){
            deleteTicketsForTrain(train);
            query.setInt(1, train.getId());
            query.executeUpdate();
        }catch (SQLException | IOException e){
            log.error(e.getMessage());}
    }

    /**
     * deletes a Station from the table STATIONS
     * @param station is the Station that will be deleted
     */
    public static void deleteStation(Station station){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("DELETE FROM STATIONS WHERE ID = ?")){
            query.setInt(1, station.getId());
            query.executeUpdate();
        }catch (SQLException | IOException e){
            log.error(e.getMessage());}
    }

    /**
     * deletes all the Tickets that are connected to a Train
     * @param train is the Train for which the Tickets are deleted
     */
    public static void deleteTicketsForTrain(Train train){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("DELETE FROM TICKETS WHERE ID = ?")){
            for(Ticket ticket: train.getTickets()){
                query.setInt(1,ticket.getId());
                query.addBatch();
            }
            query.executeBatch();
        }catch (SQLException | IOException e){
            log.error(e.getMessage());}
    }

    /**
     * updates an existing Train with new information
     * @param train is the Train that is being updated
     */
    public static void updateTrain(Train train){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("UPDATE TRAINS SET STATION=?,NAME=?,DEPARTURE=?,ARRIVAL=? WHERE ID=?")){
            query.setInt(1, train.getStationID());
            query.setString(2, train.getTrainName());
            query.setTimestamp(3,Timestamp.valueOf(train.getDepartureTime()));
            query.setTimestamp(4,Timestamp.valueOf(train.getArrivalTime()));
            query.setInt(5, train.getId());
            query.executeUpdate();
        }catch (SQLException | IOException e){
            log.error(e.getMessage());}
    }

    /**
     * updates an existing Station with new information
     * @param station is the Station that is being updated
     */
    public static void updateStation(Station station){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("UPDATE STATIONS SET NAME=?,STREET_NAME=?,STREET_NUMBER=?,POSTAL_CODE=?,CITY_NAME=? WHERE ID=?")){
            query.setString(1, station.getStationName());
            query.setString(2, station.getAddress().streetName());
            query.setString(3, station.getAddress().streetNumber());
            query.setString(4, station.getAddress().postalCode());
            query.setString(5, station.getAddress().cityName());
            query.setInt(6, station.getId());
            query.executeUpdate();
        }catch (SQLException | IOException e){
            log.error(e.getMessage());}
    }

    /**
     * sets a tickets status to RESERVED
     * @param ticket is the ticket that is being updated
     */
    public static void reserveTicket(Ticket ticket){
        try(Connection db=connectToDatabase();PreparedStatement query=db.prepareStatement("UPDATE TICKETS SET STATUS='RESERVED' WHERE ID=?")){
            query.setInt(1,ticket.getId());
            query.executeUpdate();
        }catch (SQLException | IOException e){
            log.error(e.getMessage());}
    }
}
