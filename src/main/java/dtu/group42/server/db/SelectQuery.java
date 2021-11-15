package dtu.group42.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dtu.group42.server.helpers.ArrayHelper;

public class SelectQuery implements ISelectQuery{
    private IQuery query;
    private UserTableColumn[] properties;
    private Connection con;

    public SelectQuery(IQuery query, UserTableColumn[] properties, Connection con) {
        this.query = query;
        this.properties = properties;
        this.con = con;
    }

    public String getEntity(){return query.getEntity();}
    public UserTableColumn[] getProperties(){return properties;}
    public IFilteredSelectQuery whereEquals(UserTableColumn filterProperty, String filterValue) {
        return new FilteredSelectQuery(this, filterProperty, filterValue, con);
    }

    public ResultSet execute() throws SQLException {
        var sb = new StringBuilder();
        sb.append("select ");
        sb.append(String.join(",", ArrayHelper.map(properties, prop -> prop.toString())));
        sb.append(" from ");
        sb.append(query.getEntity());
        return con.createStatement().executeQuery(sb.toString());
    }
}
