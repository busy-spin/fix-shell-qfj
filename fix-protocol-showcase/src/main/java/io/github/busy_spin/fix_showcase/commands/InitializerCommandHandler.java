package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.AppType;
import org.springframework.shell.command.annotation.Command;

@Command(command = "i", group = "QFJ Initiator Control")
public class InitializerCommandHandler extends BaseCommandHandler {
    public InitializerCommandHandler() {
        super(AppType.INITIATOR);
    }
}
