package io.github.busy_spin.fix_showcase.qfj.utils;

import lombok.Getter;
import quickfix.Log;
import quickfix.LogFactory;
import quickfix.SessionID;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueuingLogFactory implements LogFactory {

    @Getter
    private Map<SessionID, QueueLog>  logs = new ConcurrentHashMap<>();

    @Override
    public Log create(SessionID sessionID) {
        QueueLog queueLog = new QueueLog();
        logs.put(sessionID, queueLog);
        return queueLog;
    }


}
