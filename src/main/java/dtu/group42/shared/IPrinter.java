package dtu.group42.shared;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.UUID;

import dtu.group42.server.IRemoteService;

public interface IPrinter extends IRemoteService {
    /**
     * Prints file filename on the specified printer
     * 
     * @param filename
     * @param printer
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public boolean print(String filename, String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Lists the print queue for a given printer on the user's display in lines of
     * the form <job number> <file name>
     * 
     * @param printer
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @return
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public String[] queue(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Moves job to the top of the queue
     * 
     * @param printer
     * @param job
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public boolean topQueue(String printer, int job, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Starts the print server
     * 
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public boolean start(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Stops the print server
     * 
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public boolean stop(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Stops the print server, clears the print queue and starts the print server
     * again
     * 
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public boolean restart(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Prints status of printer on the user's display
     * 
     * @param printer
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @return
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public String status(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Prints the value of the parameter on the user's display
     * 
     * @param parameter
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @return
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public String readConfig(String parameter, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;

    /**
     * Sets the parameter to value
     * 
     * @param parameter
     * @param value
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     */
    public boolean setConfig(String parameter, String value, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException;
}