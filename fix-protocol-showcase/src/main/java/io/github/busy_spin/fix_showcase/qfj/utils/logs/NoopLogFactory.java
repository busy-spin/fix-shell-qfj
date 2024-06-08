package io.github.busy_spin.fix_showcase.qfj.utils.logs;

import quickfix.Log;
import quickfix.LogFactory;
import quickfix.SessionID;

public class NoopLogFactory implements LogFactory {
    @Override
    public Log create(SessionID sessionID) {
        return new NoopLog();
    }
}
