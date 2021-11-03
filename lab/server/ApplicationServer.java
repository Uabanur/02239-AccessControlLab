package server;

import java.io.FileNotFoundException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import shared.*;

public class ApplicationServer {
    public static void main(String[] args) throws RemoteException, SQLException, FileNotFoundException, InvalidAccessPolicyException{

        IUserAuthenticator auth = new UserAuthenticator();
        System.out.println("Should be true: " + auth.verifyPassword("nicu", "test123"));
        System.out.println("Should be false: " + auth.verifyPassword("nicu", "test124"));
        System.out.println("Should be true: " + auth.verifyPassword("roar", "roar_pass"));
        System.out.println("Should be false: " + auth.verifyPassword("roar", "filip_pass"));

        System.out.println();

        AccesControlManager.init();

        System.out.println("Creating registry for port: " + Config.SERVER_PORT);
        Registry registry = LocateRegistry.createRegistry(Config.SERVER_PORT);

        var services = new IRemoteService[]{
            new Printer(),
            new SessionProvider(auth),
        };

        for(var service : services) BindService(registry, service);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(SessionManager::clearExpired, 15, 15, TimeUnit.MINUTES);
    }

    private static <T extends IRemoteService> void BindService(Registry registry, T service) 
    throws RemoteException, AccessException {
        var serviceRouteName = service.ServiceRouteName();
        System.out.println("rebinding service to name: " + serviceRouteName);
        registry.rebind(serviceRouteName, service);
    }
}
