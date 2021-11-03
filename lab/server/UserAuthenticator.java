package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserAuthenticator implements IUserAuthenticator{
    private Connection _connection;

    private void connect() throws SQLException{
        _connection = AuthDatabase.getConnection();
    }

    public boolean verifyPassword(String username, String password) throws SQLException{
        if(_connection == null) connect();

        //get user from db
        String query = "select salt,password from users where username = ?";
        PreparedStatement statement = _connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        if(!result.next()) return false;

        //extract user info
        byte[] salt = result.getBytes("salt");
        byte[] dbEncryptedPassword = result.getBytes("password");

        //compare encrypted provided password with stored encrypted password from enrollment
        return Arrays.equals(PasswordHash.hashPassword(password.toCharArray(), salt), dbEncryptedPassword);
    }
}
