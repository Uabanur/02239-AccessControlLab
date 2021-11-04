package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.models.Operation;

public interface IAccessControlService {
    void init() throws SQLException, FileNotFoundException, InvalidAccessPolicyException;
    boolean verifyAccessForUser(String username, Operation op) throws SQLException;
}
