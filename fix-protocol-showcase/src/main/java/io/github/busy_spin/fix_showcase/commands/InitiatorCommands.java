package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.DefaultInitiatorController;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.util.concurrent.atomic.AtomicBoolean;

@Command(command = "init", group = "QFJ Initiator Control")
public class InitiatorCommands {

    private final DefaultInitiatorController defaultInitiatorController = new DefaultInitiatorController();

    private String defaultSession;


    public InitiatorCommands() {
        defaultInitiatorController.init();
    }

    @Command(command = "start", description = "initialize the initiator application")
    public String start() {
        defaultInitiatorController.start();
        return "";
    }

    @Command(command = "list", description = "list fix sessions")
    public String list() {
        defaultInitiatorController.printSessionIds();
        return "";
    }

    @Command(command = "default-session", description = "set the default fix session")
    public String defaultSession(@Option(longNames = "session-id", shortNames = {'s'}, required = true)
                                     String sessionId) {
        defaultSession = sessionId;
        return "Default session set to " + sessionId;
    }

    @Command(command = "log", description = "log events, incoming & outgoing messages")
    public String log(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        sessionId = getProvidedOrDefault(sessionId);

        if (sessionId != null) {
            defaultInitiatorController.printLog(sessionId);
            return "";
        } else {
            return "Session id not set";
        }
    }

    private String getProvidedOrDefault(String sessionId) {
        if (sessionId == null) {
            sessionId = defaultSession;
        }
        return sessionId;
    }

    @Command(command = "logout", description = "logout from session")
    public String stop(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        defaultInitiatorController.logout(sessionId);
        return "Logout";
    }

    @Command(command = "stop", description = "Stop initiator")
    public String stop() {
        defaultInitiatorController.stop();
        return "Initiator stopped";
    }

}
