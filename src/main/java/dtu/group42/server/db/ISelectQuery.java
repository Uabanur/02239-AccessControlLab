package dtu.group42.server.db;

public interface ISelectQuery {
    String getEntity();
    UserTableColumn[] getProperties();
    IFilteredSelectQuery whereEquals(UserTableColumn filterProperty, String filterValue);
}
