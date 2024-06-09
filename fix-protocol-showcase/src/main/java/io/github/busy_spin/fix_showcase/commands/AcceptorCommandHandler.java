package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.AppType;
import org.springframework.shell.command.annotation.Command;

@Command(command = "a", group = "QFJ Acceptor Control")
public class AcceptorCommandHandler extends BaseCommandHandler {
    public AcceptorCommandHandler() {
        super(AppType.ACCEPTOR);
    }
}
