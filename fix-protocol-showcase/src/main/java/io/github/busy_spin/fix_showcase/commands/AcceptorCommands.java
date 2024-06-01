package io.github.busy_spin.fix_showcase.commands;

import io.github.busy_spin.fix_showcase.qfj.FixInitiatorHelper;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

@Command(command = "acc", group = "QFJ Acceptor Control")
public class AcceptorCommands {

    @Command(command = "start")
    public String start(@Option(longNames = "session-id", shortNames = {'s'}) String sessionId) {
        new FixInitiatorHelper().startAcceptor();
        return "Starting " + sessionId;
    }

    @Command(command = "show")
    public String show(@Option(longNames = "type", shortNames = {'t'}) String messageType) {
        ArrayTableModel arrayTableModel = new ArrayTableModel(new String[][]{
                new String[]{"INCOMING", " alkjdlkajkl jakldjf aklj klajdfkl ajdklf jaklfdj dklaj kladjkl ajkl djdaslk jdasklj flkasdjf klasdjf"},
                new String[]{"OUTGOING", "8=FIX4.4...."}
        });

        TableBuilder tableBuilder = new TableBuilder(arrayTableModel).addHeaderAndVerticalsBorders(BorderStyle.fancy_heavy_double_dash).addHeaderBorder(BorderStyle.fancy_heavy_double_dash);

        return tableBuilder.build().render(1000);

    }

}
