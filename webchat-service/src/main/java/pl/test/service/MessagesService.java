package pl.test.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.test.dao.MessageDao;
import pl.test.model.Entity;
import pl.test.model.Message;

import java.util.List;

/**
 * Created by MSI on 18.06.2017.
 */
@Service
public class MessagesService {

    @Autowired
    private MessageDao dao;

    public List<Message> loadGroupMessages(String name) {
        List<Message> messages = dao.selectMessagesByType(name, "group");
        return messages;
    }

    public List<Message> loadPrivateMessages(String user1, String user2) {
        List<Message> messages = dao.selectMessagesByUsers(user1, user2, "user");
        return messages;
    }

    public boolean addMsg(String body) {
        JSONObject jsonBody = new JSONObject(body);
        String fromName = jsonBody.getString("from_name");
        String msg = jsonBody.getString("msg");
        String entity = jsonBody.getString("entity");
        String type = jsonBody.getString("type");

        if (dao.validateEntityType(entity, type)) {
            return dao.insertMessage(entity, fromName, msg);
        }
        return false;
    }

    public boolean validateUser(String body) {
        JSONObject jsonBody = new JSONObject(body);
        String login = jsonBody.getString("login");
        String password = jsonBody.getString("password");

        List<Entity> entities = dao.selectEntity(login, password);
        return entities.size() > 0;
    }

    public boolean createNewUser(String body) {
        JSONObject jsonBody = new JSONObject(body);
        String login = jsonBody.getString("login");
        String password = jsonBody.getString("password");
        return dao.insertUser(login, password);
    }
}
