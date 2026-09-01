package hr.javafx.data;

import hr.javafx.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UserLogEntry<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateTime timestamp;
    private final User user;
    private final String actionType;
    private final T objectBefore;
    private final T objectAfter;

    public UserLogEntry(User user, String action, T objectBefore, T objectAfter){
        this.timestamp=LocalDateTime.now();
        this.user=user;
        this.actionType=action;
        this.objectBefore=objectBefore;
        this.objectAfter=objectAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public T getObjectBefore() {
        return objectBefore;
    }

    public String getActionType() {
        return actionType;
    }

    public T getObjectAfter() {
        return objectAfter;
    }
}
