package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.FixInitiatorHelper;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "init", group = "QFJ Initiator Control")
public class InitiatorCommands {

    @Command(command = "start")
    public String start(@Option(longNames = "session-id", shortNames = {'s'}) String sessionId) {
        new FixInitiatorHelper().start();
        return "Starting " + sessionId;
    }

}
