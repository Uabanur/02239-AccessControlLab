package dtu.group42.server.startup;

import java.util.Arrays;
import java.util.stream.Stream;

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
    public static Class<?>[] getAllTypes(){
        var types = new Class<?>[][]{
            getPreWiring(),
            getConditions(),
            getConfigurations(),
            getServiceTypes(),
        };

        Stream<Class<?>> result = Stream.empty();
        for (var typeArray : types) {
            result = Stream.concat(result, Arrays.stream(typeArray));
        }
        return result.toArray(Class<?>[]::new);
    }

    public static Class<?>[] getPreWiring(){
        return new Class<?>[]{
            AppProperties.class,
        };
    }

    public static Class<?>[] getConfigurations(){
        return new Class<?>[]{
            DiConfiguration.class,
        };
    } 
    public static Class<?>[] getConditions(){
        return new Class<?>[]{
            RBACCondition.class,
            ACLCondition.class,
        };
    } 
    public static Class<?>[] getServiceTypes(){
        return new Class<?>[]{
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
