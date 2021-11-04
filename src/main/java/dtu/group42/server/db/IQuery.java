package dtu.group42.server.db;

public interface IQuery {
    String getEntity();
    ISelectQuery select(UserTableColumn... properties);
}
