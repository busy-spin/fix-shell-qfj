package io.github.busy_spin.qfj_fix_shell.qfj.utils.logs;

import quickfix.Log;
import quickfix.LogFactory;
import quickfix.SessionID;

public class NoopLogFactory implements LogFactory {
    @Override
    public Log create(SessionID sessionID) {
        return new NoopLog();
    }
}
