package dtu.group42.server;

import java.sql.SQLException;

public interface IUserAuthenticator {
    public boolean verifyPassword(String username, String password) throws SQLException;
}
