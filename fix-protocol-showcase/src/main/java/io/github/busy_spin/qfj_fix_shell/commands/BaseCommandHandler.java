package io.github.busy_spin.qfj_fix_shell.commands;

import io.github.busy_spin.qfj_fix_shell.qfj.AppType;
import io.github.busy_spin.qfj_fix_shell.qfj.DefaultFixAppLifeCycleController;
import io.github.busy_spin.qfj_fix_shell.qfj.FixAppLifeCycleController;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

public abstract class BaseCommandHandler {

    private final FixAppLifeCycleController fixAppLifeCycleController;

    private String defaultSession;

    public BaseCommandHandler(AppType appType) {
        fixAppLifeCycleController = new DefaultFixAppLifeCycleController(appType);
    }

    @Command(command = "start", description = "start fix application")
    public String start() {
        fixAppLifeCycleController.start();
        return "";
    }

    @Command(command = "list", description = "list fix sessions")
    public String list() {
        fixAppLifeCycleController.printSessionIds();
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
            fixAppLifeCycleController.printLog(sessionId);
            return "";
        } else {
            return "Session id not set";
        }
    }

    private String getProvidedOrDefault(String sessionId) {
        if (sessionId == null) {
            if (defaultSession == null) {
                defaultSession = fixAppLifeCycleController.defaultSession();
            }
            sessionId = defaultSession;
        }
        return sessionId;
    }

    @Command(command = "logout", description = "logout from session")
    public String stop(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        fixAppLifeCycleController.logout(getProvidedOrDefault(sessionId));
        return "Logout";
    }

    @Command(command = "num-in", description = "Set next number in")
    public String setNextNumIn(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId,
                               @Option(longNames = "number", shortNames = {'n'}, required = true) int number) {
        fixAppLifeCycleController.setNextNumIn(getProvidedOrDefault(sessionId), number);
        return "";
    }

    @Command(command = "num-out", description = "Set next number in")
    public String setNextNumOut(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId,
                                @Option(longNames = "number", shortNames = {'n'}, required = true) int number) {
        fixAppLifeCycleController.setNextNumOut(getProvidedOrDefault(sessionId), number);
        return "";
    }

    @Command(command = "session-details", description = "Print session details")
    public String sessionDetails(
            @Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        fixAppLifeCycleController.printSessionDetails(getProvidedOrDefault(sessionId));
        return "";
    }

    @Command(command = "stop", description = "Stop application and disconnect")
    public String stop() {
        fixAppLifeCycleController.stop();
        return "Initiator stopped";
    }

    @Command(command = "print-num", description = "Print NextNumIn and NextNumOut")
    public String printSequenceNumbers(
            @Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        fixAppLifeCycleController.printSequenceNumbers(getProvidedOrDefault(sessionId));
        return "";
    }

}
