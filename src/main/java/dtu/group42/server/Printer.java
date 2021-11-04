package dtu.group42.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtu.group42.server.services.ISecurityManager;
import dtu.group42.shared.AccessFailedException;
import dtu.group42.shared.AuthenticationFailedException;
import dtu.group42.shared.IPrinter;

@Service
public class Printer extends UnicastRemoteObject implements IPrinter {
    @Autowired private ISecurityManager securityManager;

    public Printer() throws RemoteException {
        super();
    }

    public String ServiceRouteName() {
        return "printer";
    }

    public boolean print(String filename, String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.print);
        System.out.println("print: " + filename + " - " + printer);
        return true;
    }

    public String[] queue(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.queue);
        System.out.println("queue: " + printer);
        return new String[] {};
    }

    public boolean topQueue(String printer, int job, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.topQueue);
        System.out.println("topQueue: " + printer + " - " + job);
        return true;
    }

    public boolean start(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.start);
        System.out.println("start printer");
        return true;
    }

    public boolean stop(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.stop);
        System.out.println("stop printer");
        return true;
    }

    public boolean restart(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.restart);
        System.out.println("restart printer");
        return true;
    }

    public String status(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.status);
        System.out.println("status: " + printer);
        return "ready";
    }

    public String readConfig(String parameter, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.readConfig);
        System.out.println("readConfig: " + parameter);
        return "configs";
    }

    public boolean setConfig(String parameter, String value, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, SQLException, AccessFailedException {
        securityManager.verifyAccess(sessionToken, Operation.setConfig);
        System.out.println("setConfig: " + parameter + " - " + value);
        return true;
    }
}
