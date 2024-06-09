package io.github.busy_spin.qfj_fix_shell.commands;

import io.github.busy_spin.qfj_fix_shell.qfj.AppType;
import org.springframework.shell.command.annotation.Command;

@Command(command = "a", group = "QFJ Acceptor Control")
public class AcceptorCommandHandler extends BaseCommandHandler {
    public AcceptorCommandHandler() {
        super(AppType.ACCEPTOR);
    }
}
