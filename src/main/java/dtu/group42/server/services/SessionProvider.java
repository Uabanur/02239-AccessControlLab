package dtu.group42.server.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtu.group42.shared.AuthenticationFailedException;
import dtu.group42.shared.ISessionProvider;

@Service
public class SessionProvider 
    extends UnicastRemoteObject 
    implements ISessionProvider{
    public String ServiceRouteName() { return "session"; }

    @Autowired private IUserAuthenticator _auth;
    @Autowired private ISessionService _sessionService;

    public SessionProvider() throws RemoteException{
        super();
    }

    public UUID createSession(String username, String password) throws SQLException, AuthenticationFailedException {
        if(!_auth.verifyPassword(username, password)) throw new AuthenticationFailedException("Failed to authenticate. Please try again.");
        return _sessionService.createSession(username);
    }
}
