package io.github.busy_spin.qfj_fix_shell.commands;

import io.github.busy_spin.qfj_fix_shell.qfj.AppType;
import io.github.busy_spin.qfj_fix_shell.qfj.ExchangeService;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "a", group = "QFJ Acceptor Control")
public class AcceptorCommandHandler extends BaseCommandHandler {

    private final ExchangeService exchangeService = new ExchangeService();
    public AcceptorCommandHandler() {
        super(AppType.ACCEPTOR);
    }

    @Command(command = "exec-report", description = "Send exec report")
    public String sendOrder(@Option(longNames = "session-id", shortNames = {'s'}, required = false) String sessionId) {
        sessionId = getProvidedOrDefault(sessionId);
        try {
            exchangeService.sendExecReport(sessionId);
        } catch (Exception e) {
            System.out.println(
                    AnsiOutput.toString(AnsiColor.RED, "Session not found " + sessionId, AnsiColor.DEFAULT)
            );
        }
        return "";
    }
}
