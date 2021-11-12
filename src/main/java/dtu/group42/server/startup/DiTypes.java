package dtu.group42.server.startup;

import dtu.group42.server.db.UserDatabase;
import dtu.group42.server.services.ACLService;
import dtu.group42.server.services.HashingService;
import dtu.group42.server.services.Printer;
import dtu.group42.server.services.RBACService;
import dtu.group42.server.services.SecurityManager;
import dtu.group42.server.services.SessionProvider;
import dtu.group42.server.services.SessionService;
import dtu.group42.server.services.UserAuthenticator;

public class DiTypes {
    public static Class<?>[] getTypes(){
        return new Class<?>[]{
            // app configurations
            AppConfig.class,

            // di configurations
            DiConfiguration.class,

            // di conditions
            RBACCondition.class,
            ACLCondition.class,

            // services
            HashingService.class,
            UserDatabase.class,
            SessionService.class,
            RBACService.class,
            ACLService.class,
            UserAuthenticator.class,
            SessionProvider.class,
            SecurityManager.class,
            Printer.class,
        };
    } 
}
