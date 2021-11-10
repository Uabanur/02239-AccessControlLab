package dtu.group42.shared;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.UUID;

import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.services.IRemoteService;

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
     * @throws InvalidAccessPolicyType
     */
    public boolean print(String filename, String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

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
     * @throws InvalidAccessPolicyType
     */
    public String[] queue(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

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
     * @throws InvalidAccessPolicyType
     */
    public boolean topQueue(String printer, int job, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

    /**
     * Starts the print server
     * 
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     * @throws InvalidAccessPolicyType
     */
    public boolean start(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

    /**
     * Stops the print server
     * 
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     * @throws InvalidAccessPolicyType
     */
    public boolean stop(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

    /**
     * Stops the print server, clears the print queue and starts the print server
     * again
     * 
     * @param sessionToken the session token authenticated using
     *                     {@link #createSession}
     * @throws RemoteException
     * @throws AuthenticationFailedException, AccessFailedException
     * @throws SQLException
     * @throws InvalidAccessPolicyType
     */
    public boolean restart(UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

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
     * @throws InvalidAccessPolicyType
     */
    public String status(String printer, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

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
     * @throws InvalidAccessPolicyType
     */
    public String readConfig(String parameter, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;

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
     * @throws InvalidAccessPolicyType
     */
    public boolean setConfig(String parameter, String value, UUID sessionToken)
            throws RemoteException, AuthenticationFailedException, AccessFailedException, SQLException, InvalidAccessPolicyType;
}