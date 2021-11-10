package dtu.group42.server.services;

import java.sql.SQLException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.models.Operation;
import dtu.group42.shared.AccessFailedException;
import dtu.group42.shared.AuthenticationFailedException;

@Service
public class SecurityManager implements ISecurityManager {
    @Autowired private IAccessControlService accessControl;
    @Autowired private ISessionService sessionService;

    public void verifyAccess(UUID sessionToken, Operation op)
            throws SQLException, AuthenticationFailedException, AccessFailedException, InvalidAccessPolicyType {
        var sessionData = sessionService.getSessionDataIfValid(sessionToken);
        if (sessionData == null)
            throw new AuthenticationFailedException("Invalid session");

        if (!accessControl.verifyAccessForUser(sessionData.User, op))
            throw new AccessFailedException("User does not have access to operation: " + op.toString());
    }
}
