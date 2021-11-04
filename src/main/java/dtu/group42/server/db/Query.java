package dtu.group42.server.db;

import java.sql.Connection;

public class Query implements IQuery {
    private String entity;
    private Connection con;
    public Query(String entity, Connection con){
        this.entity = entity;
        this.con = con;
    }

    public String getEntity() {return entity;}
    public ISelectQuery select(UserTableColumn... properties) {
        return new SelectQuery(this, properties, con);
    }
}
