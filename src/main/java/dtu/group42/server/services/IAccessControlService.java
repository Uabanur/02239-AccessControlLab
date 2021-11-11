package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.models.Operation;

public interface IAccessControlService {
    void init() throws FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType;
    boolean verifyAccess(String username, Operation op) throws SQLException, InvalidAccessPolicyType;
}
