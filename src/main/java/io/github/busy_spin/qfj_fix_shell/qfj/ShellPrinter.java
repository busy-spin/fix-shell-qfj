package io.github.busy_spin.qfj_fix_shell.qfj;

import io.github.busy_spin.qfj_fix_shell.qfj.utils.logs.LogElement;
import io.github.busy_spin.qfj_fix_shell.qfj.utils.logs.QueueLog;
import io.github.busy_spin.qfj_fix_shell.qfj.utils.logs.QueuingLogFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import quickfix.SessionID;
import quickfix.SessionSettings;

import java.util.*;

public class ShellPrinter {
    private final AppType appType;

    private final AnsiColor color;

    public ShellPrinter(AppType appType) {
        this.appType = appType;
        if (appType == AppType.INITIATOR) {
            color = AnsiColor.BRIGHT_BLUE;
        } else {
            color = AnsiColor.BRIGHT_RED;
        }
    }

    public void printLogs(QueuingLogFactory logFactory, String sessionId) {
        SessionID sessionID = new SessionID(sessionId);
        QueueLog queueLog = logFactory.getLogs().get(sessionID);
        if (queueLog == null) {
            return;
        }
        printMessage("Session ID " + sessionID);
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Type", "Message"});

        for (Object obj : queueLog.getQueue().toArray()) {
            LogElement element = (LogElement) obj;
            rows.add(new String[]{element.getType(), element.getMessage()});
        }

        ArrayTableModel arrayTableModel = new ArrayTableModel(rows.toArray(new String[0][0]));
        TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                .addFullBorder(BorderStyle.fancy_heavy);

        printMessage(tableBuilder.build().render(1000));
    }

    public void printSessions(SessionSettings settings) {
        ArrayList<String[]> sessions = new ArrayList<>();
        settings.sectionIterator().forEachRemaining(id -> sessions.add(new String[]{id.toString()}));
        if (sessions.isEmpty()) {
            printMessage("No sessions found");
        } else {
            ArrayTableModel arrayTableModel = new ArrayTableModel(sessions.toArray(new String[0][0]));
            TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                    .addFullBorder(BorderStyle.fancy_heavy);

            printMessage(tableBuilder.build().render(1000));
        }

    }

    public void printSessionDetails(SessionSettings settings, String sessionId) throws Exception {

        Properties properties = settings.getSessionProperties(new SessionID(sessionId));
        ArrayList<String[]> sessionDetails = new ArrayList<>();

        Properties defaultProperties = settings.getDefaultProperties();

        HashMap<Object, Object> mergeConfigs = new HashMap<>();
        mergeConfigs.putAll(defaultProperties);
        mergeConfigs.putAll(properties);

        for (Map.Entry<Object, Object> entry : mergeConfigs.entrySet()) {
            sessionDetails.add(new String[]{entry.getKey().toString(), entry.getValue().toString()});
        }

        ArrayTableModel arrayTableModel = new ArrayTableModel(sessionDetails.toArray(new String[0][0]));
        TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                .addFullBorder(BorderStyle.fancy_heavy);

        printMessage(tableBuilder.build().render(1000));
    }

    public void printSequenceNumbers(int nextNumIn, int nextNumOut, SessionID sessionID) {
        ArrayTableModel arrayTableModel = new ArrayTableModel(new String[][]{
                new String[]{"NextNumOut", String.valueOf(nextNumOut)},
                new String[]{"NextNumIn", String.valueOf(nextNumIn)}
        });
        TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                .addFullBorder(BorderStyle.fancy_heavy);

        printMessage(tableBuilder.build().render(1000));
    }

    public void printMessage(String print) {
        System.out.println(AnsiOutput.toString(color, print, AnsiColor.DEFAULT));
    }
}
