package dtu.group42.server.services;

import java.sql.SQLException;
import java.util.UUID;

import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.models.Operation;
import dtu.group42.shared.AccessFailedException;
import dtu.group42.shared.AuthenticationFailedException;

public interface ISecurityManager {
    void verifyAccess(UUID sessionToken, Operation op)
    throws SQLException, AuthenticationFailedException, AccessFailedException, InvalidAccessPolicyType;
}
