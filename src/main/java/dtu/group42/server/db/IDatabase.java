package dtu.group42.server.db;

import java.sql.SQLException;

public interface IDatabase {
    void init() throws SQLException;
    IQuery query(String entity);
}
