package io.github.busy_spin.fix_showcase;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class InitiatorCommands {

    @ShellMethod(key = "start-initiator")
    public String start(@ShellOption String sessionId) {
        new FixInitiatorHelper().start();
        return "Starting " + sessionId;
    }

    @ShellMethod(key = "start-acceptor")
    public String startAcceptor(@ShellOption String sessionId) {
        new FixInitiatorHelper().startAcceptor();
        return "Starting " + sessionId;
    }

}
