package dtu.group42.server.services;

import java.sql.SQLException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtu.group42.server.db.IDatabase;
import dtu.group42.server.db.UserTableColumn;

@Service
public class UserAuthenticator implements IUserAuthenticator{
    @Autowired private IDatabase db;
    @Autowired private IHashingService hashingService;

    @PostConstruct
    public void init() throws SQLException{
        db.init();
    }

    public boolean verifyPassword(String username, String password) throws SQLException{
        //get user from db
        var result = db.query("users")
            .select(UserTableColumn.salt, UserTableColumn.password)
            .whereEquals(UserTableColumn.username, username)
            .execute();

        if(!result.next()) return false;

        //extract user info
        byte[] salt = result.getBytes("salt");
        byte[] dbEncryptedPassword = result.getBytes("password");

        //compare encrypted provided password with stored encrypted password from enrollment
        var computedHash = hashingService.hashPassword(password.toCharArray(), salt);
        return Arrays.equals(computedHash, dbEncryptedPassword);
    }
}
