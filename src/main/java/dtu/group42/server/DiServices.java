package dtu.group42.server;

import dtu.group42.server.db.UserDatabase;
import dtu.group42.server.services.AccessControlService;
import dtu.group42.server.services.HashingService;
import dtu.group42.server.services.Printer;
import dtu.group42.server.services.SecurityManager;
import dtu.group42.server.services.SessionProvider;
import dtu.group42.server.services.SessionService;
import dtu.group42.server.services.UserAuthenticator;

public class DiServices {
    public static Class<?>[] getServiceTypes(){
        return new Class<?>[]{
            HashingService.class,
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
