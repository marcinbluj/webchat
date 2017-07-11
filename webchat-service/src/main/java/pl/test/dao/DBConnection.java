package pl.test.dao;

import java.sql.Connection;

/**
 * Created by MSI on 19.06.2017.
 */
public interface DBConnection {
    Connection connect();

    void disconnect();
}
