package pl.test.dao;

import org.springframework.stereotype.Repository;
import pl.test.model.Entity;
import pl.test.model.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MSI on 18.06.2017.
 */
@Repository
public class MessageDaoImpl implements MessageDao {

    private DBConnection connection;

    public MessageDaoImpl() {
        connection = new MysqlConnection();
    }

    @Override
    public boolean insertMessage(String toName, String fromName, String msg) {
        String insertMessage = "INSERT INTO chat_messages(to_name, from_name, body) VALUE (?,?,?);";
        try {
            PreparedStatement ps = connection.connect().prepareStatement(insertMessage);
            ps.setString(1, toName);
            ps.setString(2, fromName);
            ps.setString(3, msg);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            connection.disconnect();
        }
        return true;
    }

    @Override
    public List<Message> selectMessages(String toName) {
        String selectMessage = "SELECT * FROM chat_messages WHERE to_name = '" + toName + "' ORDER BY time DESC;";
        List<Message> msgs = new LinkedList<>();

        try {
            PreparedStatement ps = connection.connect().prepareStatement(selectMessage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                msgs.add(extractMessage(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.disconnect();
        }
        return msgs;
    }

    @Override
    public List<Message> selectMessagesByType(String name, String entityType) {
        String selectByType = "select chat_messages.* from chat_messages " +
                "inner join chat_entity on to_name = name " +
                "where to_name = '" + name + "' " +
                "and chat_entity.type = '" + entityType + "' " +
                "order by time DESC;";
        List<Message> msgs = new LinkedList<>();

        try {
            PreparedStatement ps = connection.connect().prepareStatement(selectByType);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                msgs.add(extractMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.disconnect();
        }
        return msgs;
    }

    @Override
    public List<Message> selectMessagesByUsers(String user1, String user2, String entityType) {
        String selectPrivateMessages = "select chat_messages.* from chat_messages " +
                "inner join chat_entity on to_name = name and type = 'user' " +
                "where (to_name = '" + user2 + "' and from_name = '" + user1 + "') " +
                "or (to_name = '" + user1 + "' and from_name = '" + user2 + "') " +
                "order by time DESC;";
        List<Message> msgs = new LinkedList<>();

        try {
            PreparedStatement ps = connection.connect().prepareStatement(selectPrivateMessages);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                msgs.add(extractMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.disconnect();
        }
        return msgs;
    }

    @Override
    public List<Entity> selectEntity(String login, String password) {
        String selectEntity = "select * from chat_entity " +
                "where (" +
                "name = '" + login + "' " +
                "and password = '" + password + "' " +
                "and type = 'user');";
        List<Entity> entities = new LinkedList<>();

        try {
            PreparedStatement ps = connection.connect().prepareStatement(selectEntity);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name;
                String log;
                String pas;
                String type;
                long id;
                name = rs.getString("name");
                log = rs.getString("login");
                pas = rs.getString("password");
                type = rs.getString("type");
                id = rs.getLong("id");
                entities.add(new Entity(name, log, pas, type, id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            connection.disconnect();
        }
        return entities;
    }

    @Override
    public boolean insertUser(String login, String password) {
        String insertUser = "insert into chat_entity(name, login, password, type) " +
                "value (?, ?, ?, 'user');";
        try {
            PreparedStatement ps = connection.connect().prepareStatement(insertUser);
            ps.setString(1, login);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean validateEntityType(String entity, String type) {
        String selectEntity = "select * from chat_entity where (name = '" + entity + "' and type = '" + type + "');";
        boolean validation = false;

        try {
            PreparedStatement ps = connection.connect().prepareStatement(selectEntity);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                validation = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            connection.disconnect();
        }
        return validation;
    }

    private Message extractMessage(ResultSet rs) throws SQLException {
        String to;
        String from;
        String body;
        Timestamp timestamp;
        long id;
        to = rs.getString("to_name");
        from = rs.getString("from_name");
        body = rs.getString("body");
        timestamp = rs.getTimestamp("time");
        id = rs.getLong("id");

        return new Message.MsgBuilder()
                .toName(to)
                .fromName(from)
                .body(body)
                .timestamp(timestamp)
                .id(id)
                .build();
    }
}
