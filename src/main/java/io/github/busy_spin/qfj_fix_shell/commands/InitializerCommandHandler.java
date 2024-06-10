package io.github.busy_spin.qfj_fix_shell.commands;

import io.github.busy_spin.qfj_fix_shell.qfj.AppType;
import io.github.busy_spin.qfj_fix_shell.qfj.TakerFirmService;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "i", group = "QFJ Initiator Control")
public class InitializerCommandHandler extends BaseCommandHandler {

    private final TakerFirmService takerFirmService = new TakerFirmService();

    public InitializerCommandHandler() {
        super(AppType.INITIATOR);
    }

    @Command(command = "send-order", description = "Send order")
    public String sendOrder(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        sessionId = getProvidedOrDefault(sessionId);
        try {
            takerFirmService.sendOrder(sessionId);
        } catch (Exception e) {
            System.out.println(
                    AnsiOutput.toString(AnsiColor.RED, "Session not found " + sessionId, AnsiColor.DEFAULT)
            );
        }
        return "";
    }
}
