package io.github.busy_spin.fix_showcase;

import quickfix.*;

public class FixInitiatorHelper {

    public boolean start() {
        try {
            SessionSettings sessionSettings = new SessionSettings(FileUtil.open(FixInitiatorHelper.class, "initiator.cfg"));
            SocketInitiator socketInitiator = new SocketInitiator(
                    new InitiatorApplication(), new NoopStoreFactory(), sessionSettings, new ScreenLogFactory(), new DefaultMessageFactory());
            socketInitiator.start();
            return true;
        } catch (ConfigError e) {
            e.printStackTrace();
            return false;
        }
    }

}
