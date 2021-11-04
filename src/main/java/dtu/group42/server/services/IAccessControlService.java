package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import dtu.group42.server.InvalidAccessPolicyException;
import dtu.group42.server.Operation;

public interface IAccessControlService {
    void init() throws SQLException, FileNotFoundException, InvalidAccessPolicyException;
    boolean verifyAccessForUser(String username, Operation op) throws SQLException;
}
