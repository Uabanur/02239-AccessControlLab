package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.UUID;

import shared.AuthenticationFailedException;
import shared.ISessionProvider;

public class SessionProvider 
    extends UnicastRemoteObject 
    implements ISessionProvider{
    public String ServiceRouteName() { return "session"; }

    private IUserAuthenticator _auth;
    public SessionProvider(IUserAuthenticator auth) throws RemoteException{
        super();
        this._auth = auth;
    }

    public UUID createSession(String username, String password) throws SQLException, AuthenticationFailedException {
        if(!_auth.verifyPassword(username, password)) throw new AuthenticationFailedException("Failed to authenticate. Please try again.");
        return SessionManager.createSession(username);
    }
}
