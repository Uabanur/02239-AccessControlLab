package dtu.group42.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IFilteredSelectQuery {
    ResultSet execute() throws SQLException;
}
