package server;

import java.sql.SQLException;
import java.util.UUID;

import shared.AccessFailedException;
import shared.AuthenticationFailedException;

public class SecurityManager {
    public static void verifyAccess(UUID sessionToken, Operation op) 
    throws SQLException, AuthenticationFailedException, AccessFailedException {
        var sessionData = SessionManager.getSessionDataIfValid(sessionToken);
        if (sessionData == null)
            throw new AuthenticationFailedException("Invalid session");

        if (!AccesControlManager.verifyAccessForUser(sessionData.User, op))
            throw new AccessFailedException("User does not have access to operation: " + op.toString());
    }
}
