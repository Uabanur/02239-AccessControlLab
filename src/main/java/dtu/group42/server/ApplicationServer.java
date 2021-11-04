package dtu.group42.server;

import java.io.FileNotFoundException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dtu.group42.server.services.SessionService;
import dtu.group42.shared.*;

public class ApplicationServer {
    public static void main(String[] args)
            throws RemoteException, SQLException, FileNotFoundException, InvalidAccessPolicyException {

        // IUserAuthenticator auth = new UserAuthenticator();
        // auth.init();
        // System.out.println("Should be true: " + auth.verifyPassword("nicu",
        // "test123"));
        // System.out.println("Should be false: " + auth.verifyPassword("nicu",
        // "test124"));
        // System.out.println("Should be true: " + auth.verifyPassword("roar",
        // "roar_pass"));
        // System.out.println("Should be false: " + auth.verifyPassword("roar",
        // "filip_pass"));
        // System.out.println();

        try (var ctx = new AnnotationConfigApplicationContext(DiServices.getServiceTypes())) {
            configureServices(ctx);
            configureBatchJobs(ctx);
        }
    }

    private static void configureBatchJobs(AnnotationConfigApplicationContext ctx) {
        var service = ctx.getBean(SessionService.class);
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(() -> service.clearExpired(), 15, 15, TimeUnit.MINUTES);
    }

    private static void configureServices(AnnotationConfigApplicationContext ctx)
            throws SQLException, AccessException, BeansException, RemoteException {
        System.out.println("Creating registry for port: " + Config.SERVER_PORT);
        Registry registry = LocateRegistry.createRegistry(Config.SERVER_PORT);

        BindService(registry, ctx.getBean(SessionProvider.class));
        BindService(registry, ctx.getBean(Printer.class));
    }

    private static <T extends IRemoteService> void BindService(Registry registry, T service)
            throws RemoteException, AccessException {
        var serviceRouteName = service.ServiceRouteName();
        System.out.println("rebinding service to name: " + serviceRouteName);
        registry.rebind(serviceRouteName, service);
    }
}
