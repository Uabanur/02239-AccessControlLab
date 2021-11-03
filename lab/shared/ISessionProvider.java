package shared;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.UUID;

import server.IRemoteService;

public interface ISessionProvider extends IRemoteService {
    /**
     * Creates a limited authentication session token for the user.
     * @return Session token.
     * @throws RemoteException
     * @throws AuthenticationFailedException if username/password pair is invalid.
     * @throws SQLException
     */
    public UUID createSession(String username, String password) throws RemoteException, SQLException;
}
