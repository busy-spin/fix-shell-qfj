package io.github.busy_spin.fix_showcase.qfj;

import quickfix.*;

public class FixInitiatorHelper {

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
                    new NoopLogFactory(),
                    new DefaultMessageFactory());
            socketAcceptor.start();
            return true;
        } catch (ConfigError e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class NoopLogFactory implements LogFactory {

        @Override
        public Log create(SessionID sessionID) {
            return new Log() {
                @Override
                public void clear() {

                }

                @Override
                public void onIncoming(String s) {

                }

                @Override
                public void onOutgoing(String s) {

                }

                @Override
                public void onEvent(String s) {

                }

                @Override
                public void onErrorEvent(String s) {

                }
            };
        }
    }

}
