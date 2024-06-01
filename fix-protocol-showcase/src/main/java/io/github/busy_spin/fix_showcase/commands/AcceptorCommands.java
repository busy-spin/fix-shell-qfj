package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.FixInitiatorHelper;
import io.github.busy_spin.fix_showcase.qfj.utils.LogElement;
import io.github.busy_spin.fix_showcase.qfj.utils.QueueLog;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Command(command = "acc", group = "QFJ Acceptor Control")
public class AcceptorCommands {

    FixInitiatorHelper fixInitiatorHelper = new FixInitiatorHelper();

    @Command(command = "start")
    public String start(@Option(longNames = "session-id", shortNames = {'s'}) String sessionId) {
        fixInitiatorHelper.startAcceptor();
        return "Starting " + sessionId;
    }

    @Command(command = "show")
    public String show(@Option(longNames = "type", shortNames = {'t'}) String messageType) {

        Collection<QueueLog> queueLogs = fixInitiatorHelper.getLogFactory().getLogs().values();
        List<String[]> items = queueLogs.stream().flatMap(log -> log.getQueue().stream())
                .map(item -> new String[]{item.getType(), item.getMessage()})
                .collect(Collectors.toList());

        String[][] data = new String[items.size()][2];
        for (int i = 0; i < data.length; i++) {
            data[i] = items.get(i);
        }

        ArrayTableModel arrayTableModel = new ArrayTableModel(data);

        TableBuilder tableBuilder = new TableBuilder(arrayTableModel)
                .addHeaderAndVerticalsBorders(BorderStyle.fancy_heavy_double_dash)
                .addHeaderBorder(BorderStyle.fancy_heavy_double_dash);

        return tableBuilder.build().render(1000);

    }

}
