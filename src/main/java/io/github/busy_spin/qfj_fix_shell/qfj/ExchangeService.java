package io.github.busy_spin.qfj_fix_shell.qfj;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;

public class ExchangeService {

    public void sendExecReport(String sessionId) throws SessionNotFound {
        ExecutionReport report = new ExecutionReport(new OrderID("abc"), new ExecID("bac"),
                new ExecType(ExecType.FILL), new OrdStatus(OrdStatus.FILLED),
                new Side(Side.BUY), new LeavesQty(0), new CumQty(1000), new AvgPx(120.0));
        report.set(new Symbol("BNA"));
        Session.sendToTarget(report, new SessionID(sessionId));
    }
}
