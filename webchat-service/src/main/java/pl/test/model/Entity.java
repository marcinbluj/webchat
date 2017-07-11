package pl.test.model;

/**
 * Created by MSI on 17.06.2017.
 */
public class Entity {
    private String name;
    private String login;
    private String password;
    private String type;
    private long id;

    public Entity(String name, String login, String password, String type, long id) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
