package dtu.group42;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dtu.group42.server.models.Operation;
import dtu.group42.server.services.Printer;
import dtu.group42.server.services.SessionProvider;
import dtu.group42.server.startup.DiTypes;
import dtu.group42.shared.AccessFailedException;

public class AccessTest 
{
    private static Printer printer;
    private static SessionProvider sessionProvider;

    @BeforeAll
    public static void setup(){
        try (var ctx = new AnnotationConfigApplicationContext(DiTypes.getTypes())){
            printer = ctx.getBean(Printer.class);
            sessionProvider = ctx.getBean(SessionProvider.class);
        }
    }

    @ParameterizedTest
    @CsvSource({
        "alice,alice123",
        "bob,bob123",
        "cecilia,cecilia123",
        "david,david123",
        "erica,erica123",
        "fred,fred123",
        "george,george123",
    })
    public void canLogInTest(String username, String password) 
        throws Exception
    {
        var token = sessionProvider.createSession(username, password);
        assertNotNull(token);
    }

    @ParameterizedTest
    @CsvSource({
        "alice,alice123,print;queue;topQueue;start;stop;restart;status;readConfig;setConfig",
        "bob,bob123,start;stop;restart;status;readConfig;setConfig",
        "cecilia,cecilia123,print;queue;topQueue;restart",
        "david,david123,print;queue",
        "erica,erica123,print;queue",
        "fred,fred123,print;queue",
        "george,george123,print;queue",
    }) 
    public void hasStrictAccessTest(String username, String password, String operations)
        throws Exception
    {
        var allOperations = Operation.values();
        var requiredOperations = Arrays
            .stream(operations.split(";"))
            .map(Operation::valueOf)
            .toArray(Operation[]::new);

        var token = sessionProvider.createSession(username, password);

        // verify that all specified operations are allowed
        for (var op : requiredOperations) {
            performOperation(op, token);
        }

        //verify that all non-specified operations are disallowed
        for (var op : allOperations) {
            if(Arrays.stream(requiredOperations).anyMatch(op::equals)) continue;
            assertThrows(AccessFailedException.class, () -> {
                performOperation(op, token);
            });
        }
    }

    private static void performOperation(Operation op, UUID token)
        throws Exception
    {
        switch(op){
            case print: printer.print("filename", "printer", token); return;
            case queue: printer.queue("printer", token); return;
            case topQueue: printer.topQueue("printer", 0, token); return;
            case start: printer.start(token); return;
            case stop: printer.stop(token); return;
            case restart: printer.restart(token); return;
            case status: printer.status("printer", token); return;
            case readConfig: printer.readConfig("parameter", token); return;
            case setConfig: printer.setConfig("parameter", "value", token); return;
        }
    }
}
