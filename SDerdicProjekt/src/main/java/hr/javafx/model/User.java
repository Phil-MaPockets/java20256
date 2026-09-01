package hr.javafx.model;

import hr.javafx.data.JsonFileManager;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String name;
    private String surname;
    private String password;
    private UserRole role;
    private List<Ticket> tickets=new ArrayList<>();

    public User(String name, String surname, String password, String username, UserRole role) {
        this.id= JsonFileManager.createObjects("Users.json",new ArrayList<User>(){}.getClass().getGenericSuperclass()).size();
        this.username=username;
        this.name = name;
        this.surname = surname;
        this.password=password;
        this.role=role;
    }

    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
