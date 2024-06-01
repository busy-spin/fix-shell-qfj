package io.github.busy_spin.fix_showcase.qfj;

import io.github.busy_spin.fix_showcase.qfj.utils.NoopLogFactory;
import io.github.busy_spin.fix_showcase.qfj.utils.QueuingLogFactory;
import lombok.Getter;
import quickfix.*;

public class FixInitiatorHelper {

    @Getter
    private QueuingLogFactory logFactory = new QueuingLogFactory();

    public boolean start() {
        try {
            SessionSettings sessionSettings =
                    new SessionSettings(FileUtil.open(FixInitiatorHelper.class, "initiator.cfg"));
            SocketInitiator socketInitiator = new SocketInitiator(
                    new InitiatorApplication(),
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
                    new SessionSettings(FileUtil.open(FixInitiatorHelper.class, "acceptor.cfg"));
            SocketAcceptor socketAcceptor = new SocketAcceptor(
                    new InitiatorApplication(),
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

}
