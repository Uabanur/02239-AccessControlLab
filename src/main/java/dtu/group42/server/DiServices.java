package dtu.group42.server;

import dtu.group42.server.db.UserDatabase;
import dtu.group42.server.services.AccessControlService;
import dtu.group42.server.services.SecurityManager;
import dtu.group42.server.services.SessionService;

public class DiServices {
    public static Class<?>[] getServiceTypes(){
        return new Class<?>[]{
            UserDatabase.class,
            SessionService.class,
            AccessControlService.class,
            UserAuthenticator.class,
            SessionProvider.class,
            SecurityManager.class,
            Printer.class,
        };
    } 
}
