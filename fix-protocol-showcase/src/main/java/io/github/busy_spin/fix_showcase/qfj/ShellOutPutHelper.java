package io.github.busy_spin.fix_showcase.qfj;

import io.github.busy_spin.fix_showcase.qfj.utils.logs.LogElement;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.NoopLogFactory;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueueLog;
import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueuingLogFactory;
import lombok.Getter;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import quickfix.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ShellOutPutHelper {

    @Getter
    private QueuingLogFactory logFactory = new QueuingLogFactory();

    public boolean start() {
        try {
            SessionSettings sessionSettings =
                    new SessionSettings(FileUtil.open(ShellOutPutHelper.class, "initiator.cfg"));
            SocketInitiator socketInitiator = new SocketInitiator(
                    new Application(),
                    new NoopStoreFactory(),
                    sessionSettings,
                    new NoopLogFactory(),
                    new DefaultMessageFactory());
            socketInitiator.start();
            return true;
        } catch (ConfigError e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean startAcceptor() {
        try {
            SessionSettings sessionSettings =
                    new SessionSettings(FileUtil.open(ShellOutPutHelper.class, "acceptor.cfg"));
            SocketAcceptor socketAcceptor = new SocketAcceptor(
                    new Application(),
                    new NoopStoreFactory(),
                    sessionSettings,
                    logFactory,
                    new DefaultMessageFactory());
            socketAcceptor.start();
            return true;
        } catch (ConfigError e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printLogs(QueuingLogFactory logFactory) {
        Map<SessionID, QueueLog> sessionLogs = logFactory.getLogs();
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

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            sessionDetails.add(new String[]{entry.getKey().toString(), entry.getValue().toString()});
        }

        ArrayTableModel arrayTableModel = new ArrayTableModel(sessionDetails.toArray(new String[0][0]));
        TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                .addFullBorder(BorderStyle.fancy_heavy);

        System.out.println(tableBuilder.build().render(1000));
    }

}
