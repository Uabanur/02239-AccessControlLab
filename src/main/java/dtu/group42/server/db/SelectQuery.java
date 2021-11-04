package dtu.group42.server.db;

import java.sql.Connection;

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
}
