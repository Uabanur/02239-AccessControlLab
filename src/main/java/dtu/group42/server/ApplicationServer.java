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
import org.springframework.core.env.Environment;

import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.services.IRemoteService;
import dtu.group42.server.services.SessionService;
import dtu.group42.server.startup.DiTypes;

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

        try (var ctx = new AnnotationConfigApplicationContext(DiTypes.getTypes())){
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
        var env = ctx.getBean(Environment.class);
        var serverPort = Integer.parseInt(env.getProperty("server.port"));
        System.out.println("Creating registry for port: " + serverPort);
        Registry registry = LocateRegistry.createRegistry(serverPort);

        var remoteServices = ctx.getBeansOfType(IRemoteService.class);
        for (var service : remoteServices.values())
            BindService(registry, service);
    }

    private static <T extends IRemoteService> void BindService(Registry registry, T service)
            throws RemoteException, AccessException {
        var serviceRouteName = service.ServiceRouteName();
        System.out.println("rebinding service to name: " + serviceRouteName);
        registry.rebind(serviceRouteName, service);
    }
}
