package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.BaseFixLifeCycleController;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

public class BaseCommandHandler {

    private final BaseFixLifeCycleController defaultInitiatorController = new BaseFixLifeCycleController();

    private String defaultSession;

    public BaseCommandHandler() {
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
            if (defaultSession == null) {
                defaultSession = defaultInitiatorController.defaultSession();
            }
            sessionId = defaultSession;
        }
        return sessionId;
    }

    @Command(command = "logout", description = "logout from session")
    public String stop(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        defaultInitiatorController.logout(getProvidedOrDefault(sessionId));
        return "Logout";
    }

    @Command(command = "num-in", description = "Set next number in")
    public String setNextNumIn(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId,
                               @Option(longNames = "number", shortNames = {'n'}, required = true) int number) {
        defaultInitiatorController.setNextNumIn(getProvidedOrDefault(sessionId), number);
        return "";
    }

    @Command(command = "num-out", description = "Set next number in")
    public String setNextNumOut(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId,
                               @Option(longNames = "number", shortNames = {'n'}, required = true) int number) {
        defaultInitiatorController.setNextNumOut(getProvidedOrDefault(sessionId), number);
        return "";
    }

    @Command(command = "session-details", description = "Print session details")
    public String sessionDetails(
            @Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        defaultInitiatorController.printSessionDetails(getProvidedOrDefault(sessionId));
        return "";
    }

    @Command(command = "stop", description = "Stop initiator")
    public String stop() {
        defaultInitiatorController.stop();
        return "Initiator stopped";
    }

}
