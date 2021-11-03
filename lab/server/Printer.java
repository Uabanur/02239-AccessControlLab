package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.UUID;

import shared.AccessFailedException;
import shared.AuthenticationFailedException;
import shared.IPrinter;

public class Printer extends UnicastRemoteObject implements IPrinter {
    public String ServiceRouteName() {
        return "printer";
    }

    public Printer() throws RemoteException {
        super();
    }

    public boolean print(String filename, String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.print);
        System.out.println("print: " + filename + " - " + printer);
        return true;
    }

    public String[] queue(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.queue);
        System.out.println("queue: " + printer);
        return new String[] {};
    }

    public boolean topQueue(String printer, int job, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.topQueue);
        System.out.println("topQueue: " + printer + " - " + job);
        return true;
    }

    public boolean start(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.start);
        System.out.println("start printer");
        return true;
    }

    public boolean stop(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.stop);
        System.out.println("stop printer");
        return true;
    }

    public boolean restart(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.restart);
        System.out.println("restart printer");
        return true;
    }

    public String status(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.status);
        System.out.println("status: " + printer);
        return "ready";
    }

    public String readConfig(String parameter, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.readConfig);
        System.out.println("readConfig: " + parameter);
        return "configs";
    }

    public boolean setConfig(String parameter, String value, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        SecurityManager.verifyAccess(sessionToken, Operation.setConfig);
        System.out.println("setConfig: " + parameter + " - " + value);
        return true;
    }
}
