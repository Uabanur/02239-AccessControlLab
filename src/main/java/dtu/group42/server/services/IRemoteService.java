package dtu.group42.server.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteService extends Remote {
    public String ServiceRouteName() throws RemoteException;
}
