package io.github.busy_spin.fix_showcase.qfj;

import io.github.busy_spin.fix_showcase.qfj.utils.logs.LogElement;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueueLog;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueuingLogFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import quickfix.SessionID;
import quickfix.SessionSettings;

import java.util.*;

public class ShellPrinter {


    public void printLogs(QueuingLogFactory logFactory) {
        Map<SessionID, QueueLog> sessionLogs = logFactory.getLogs();
        for (SessionID sessionID : sessionLogs.keySet()) {
            AnsiOutput.toString(AnsiColor.BRIGHT_BLUE,
                    "Session ID" + sessionID.toString(), AnsiColor.DEFAULT);
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
    }

    public void printSessions(SessionSettings settings) {
        ArrayList<String[]> sessions = new ArrayList<>();
        settings.sectionIterator().forEachRemaining(id -> sessions.add(new String[]{id.toString()}));
        if (sessions.isEmpty()) {
            System.out.println("No sessions found");
        } else {
            ArrayTableModel arrayTableModel = new ArrayTableModel(sessions.toArray(new String[0][0]));
            TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                    .addFullBorder(BorderStyle.fancy_heavy);

            System.out.println(tableBuilder.build().render(1000));
        }

    }

    public void printSessionDetails(SessionSettings settings, String sessionId) throws Exception {

        Properties properties = settings.getSessionProperties(new SessionID(sessionId));
        ArrayList<String[]> sessionDetails = new ArrayList<>();

        Properties defaultProperties = settings.getDefaultProperties();

        HashMap<Object, Object> mergeConfigs = new HashMap<>();
        for (Map.Entry<Object, Object> entry : defaultProperties.entrySet()) {
            mergeConfigs.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            mergeConfigs.put(entry.getKey(), entry.getValue());
        }


        for (Map.Entry<Object, Object> entry : mergeConfigs.entrySet()) {
            sessionDetails.add(new String[]{entry.getKey().toString(), entry.getValue().toString()});
        }

        ArrayTableModel arrayTableModel = new ArrayTableModel(sessionDetails.toArray(new String[0][0]));
        TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                .addFullBorder(BorderStyle.fancy_heavy);

        System.out.println(tableBuilder.build().render(1000));
    }

}
