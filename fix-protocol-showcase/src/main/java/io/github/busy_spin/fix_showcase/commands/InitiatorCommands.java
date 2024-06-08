package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.DefaultInitiatorController;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.util.concurrent.atomic.AtomicBoolean;

@Command(command = "init", group = "QFJ Initiator Control")
public class InitiatorCommands {

    private final DefaultInitiatorController defaultInitiatorController = new DefaultInitiatorController();

    private String defaultSession;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Command(command = "start", description = "initialize the initiator application")
    public String start() {
        if (initialized.compareAndSet(false, true)) {
            defaultInitiatorController.init();
            defaultInitiatorController.start();
            return "Initiator(s) started";
        } else {
            return "Initiators already started.";
        }
    }

    @Command(command = "list", description = "list fix sessions")
    public String list() {
        if (!initialized.get()) {
            return "Initialize first";
        }

        defaultInitiatorController.printSessionIds();
        return "";
    }

    @Command(command = "default-session", description = "set the default fix session")
    public String defaultSession(@Option(longNames = "session-id", shortNames = {'s'}, required = true)
                                     String sessionId) {
        if (!initialized.get()) {
            return "Initialize first";
        }
        defaultSession = sessionId;
        return "Default session set";
    }

    @Command(command = "log", description = "log events, incoming & outgoing messages")
    public String log(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        if (!initialized.get()) {
            return "Initialize first";
        }

        if (sessionId == null) {
            sessionId = defaultSession;
        }

        if (sessionId != null) {
            defaultInitiatorController.printLog(sessionId);
            return "END LOG";
        } else {
            return "Session id not set";
        }
    }

}
