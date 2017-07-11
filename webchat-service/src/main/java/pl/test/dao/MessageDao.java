package pl.test.dao;

import pl.test.model.Entity;
import pl.test.model.Message;

import java.util.List;

/**
 * Created by MSI on 18.06.2017.
 */
public interface MessageDao {
    boolean insertMessage(String toName, String fromName, String msg);

    List<Message> selectMessages(String toName);

    List<Message> selectMessagesByType(String name, String entityType);

    List<Message> selectMessagesByUsers(String fromName, String toName, String entityType);

    List<Entity> selectEntity(String login, String password);

    boolean insertUser(String login, String password);

    boolean validateEntityType(String entity, String type);
}
