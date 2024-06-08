package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.ShellOutPutHelper;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.LogElement;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueueLog;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import quickfix.SessionID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Command(command = "acc", group = "QFJ Acceptor Control")
public class AcceptorCommands {

    ShellOutPutHelper shellOutPutHelper = new ShellOutPutHelper();

    @Command(command = "start")
    public String start(@Option(longNames = "session-id", shortNames = {'s'}) String sessionId) {
        shellOutPutHelper.startAcceptor();
        return "Starting " + sessionId;
    }

    @Command(command = "color-test")
    public String colorTest(@Option(longNames = "session-id", shortNames = {'s'}) String sessionId) {
        return AnsiOutput.toString(AnsiColor.RED, "Session ID" + "ABC", AnsiColor.DEFAULT);
    }

    @Command(command = "show")
    public String show(@Option(longNames = "type", shortNames = {'t'}) String messageType) {


        Map<SessionID, QueueLog> sessionLogs = shellOutPutHelper.getLogFactory().getLogs();
        for (SessionID sessionID : sessionLogs.keySet()) {
            AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Session ID" + sessionID.toString(), AnsiColor.DEFAULT);
            System.out.println("Session ID " + sessionID);
            List<String[]> rows = new ArrayList<>();
            rows.add(new String[]{"Event", "Message"});

            QueueLog queueLog = sessionLogs.get(sessionID);
            for (Object obj : queueLog.getQueue().toArray()) {
                LogElement element = (LogElement) obj;
                rows.add(new String[]{element.getType(), element.getMessage()});
            }

            ArrayTableModel arrayTableModel = new ArrayTableModel(rows.toArray(new String[0][0]));
            TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                    .addFullBorder(BorderStyle.fancy_heavy);

            System.out.println(tableBuilder.build().render(1000));
        }

        return null;

    }

}
