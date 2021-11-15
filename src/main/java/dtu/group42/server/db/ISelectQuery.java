package dtu.group42.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISelectQuery {
    String getEntity();
    UserTableColumn[] getProperties();
    IFilteredSelectQuery whereEquals(UserTableColumn filterProperty, String filterValue);
    ResultSet execute() throws SQLException;
}
