package dtu.group42.server.db;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtu.group42.server.services.IHashingService;

@Service
public class UserDatabase implements IDatabase {
    @Autowired IHashingService hashingService;

    private boolean initialied = false;
    private Connection _connection;
    private Random RANDOM = new SecureRandom();

    public IQuery query(String entity){
        return new Query(entity, _connection);
    }

    @PostConstruct
    public void init() throws SQLException{
        if(initialied) return;
         _connection = createInMemoryDatabase();
         populateTestData(_connection);
        // printTestData(_connection);
         initialied = true;
    }
    
    private Connection createInMemoryDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    private void populateTestData(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate("create table users (username varchar(30), password BLOB(64), salt BLOB(64), roles text)");
        String queryString = "insert into users values(?, ?, ?, ?)";
        PreparedStatement stm = con.prepareStatement(queryString);

        String[] users = new String[]{ 
           // "nicu/test123/user",
           // "alex/alex_pass/user",
           // "filip/filip_pass/user",
           // "roar/roar_pass/user",
            "alice/alice123/manager",
           // "bob/bob123/janitor,technician",
            "cecilia/cecilia123/power_user",
            "david/david123/user",
            "erica/erica123/user",
            "fred/fred123/user",
            "george/george123/user",
            "ida/ida123/power_user",
            "henry/henry123/user",

        };

        for (String user : users) {
            String[] parts = user.split("/");
            String username = parts[0];
            String password = parts[1];
            String roles = parts[2];
            stm.setString(1, username);
            byte[] salt = new byte[512];
            RANDOM.nextBytes(salt);
            stm.setBytes(2, hashingService.hashPassword(password.toCharArray(), salt));
            stm.setBytes(3, salt);
            stm.setString(4, roles);
            stm.executeUpdate();
        }
    }

    private void printTestData(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("select * from users");
        System.out.println("User test data:");
        while(rs.next())
        {
            System.out.println("username: " + rs.getString("username") + " roles: " + rs.getString("roles"));
        }
        System.out.println();
    }
}

