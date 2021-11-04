package dtu.group42.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dtu.group42.server.helpers.ArrayHelper;

public class FilteredSelectQuery implements IFilteredSelectQuery {
    private ISelectQuery selectQuery;
    private UserTableColumn filterProperty;
    private String filterValue;
    private Connection con;

    public FilteredSelectQuery(ISelectQuery selectQuery, UserTableColumn prop, String filterValue, Connection con){
        this.selectQuery = selectQuery;
        this.filterProperty = prop;
        this.filterValue = filterValue;
        this.con = con;
    }

    public ResultSet execute() throws SQLException {
        //String query = "select salt,password from users where username = ?";

        var sb = new StringBuilder();
        sb.append("select ");
        sb.append(String.join(",", ArrayHelper.map(selectQuery.getProperties(), prop -> prop.toString())));
        sb.append(" from ");
        sb.append(selectQuery.getEntity());
        sb.append(" where ");
        sb.append(filterProperty.toString());
        sb.append(" = ?");
        var statement = con.prepareStatement(sb.toString());
        statement.setString(1, filterValue);
        return statement.executeQuery();
    }
}
