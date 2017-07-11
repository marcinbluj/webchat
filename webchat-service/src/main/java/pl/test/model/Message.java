package pl.test.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * Created by MSI on 17.06.2017.
 */
public class Message implements Serializable {
    private String fromName;
    private String toName;
    private String body;
    private Timestamp timestamp;
    private long id;

    public Message() {
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + fromName + ": "
                + body;
    }

    public static class MsgBuilder {
        Message msg;

        public MsgBuilder() {
            this.msg = new Message();
        }

        public MsgBuilder toName(String toName) {
            msg.setToName(toName);
            return this;
        }

        public MsgBuilder fromName(String fromName) {
            msg.setFromName(fromName);
            return this;
        }

        public MsgBuilder body(String body) {
            msg.setBody(body);
            return this;
        }

        public MsgBuilder timestamp(Timestamp timestamp) {
            msg.setTimestamp(timestamp);
            return this;
        }

        public MsgBuilder id(long id) {
            msg.setId(id);
            return this;
        }

        public Message build() {
            return msg;
        }
    }
}
