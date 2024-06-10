package io.github.busy_spin.qfj_fix_shell.qfj;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.NewOrderSingle;

public class TraderService {

    public void sendOrder(String sessionId) throws SessionNotFound {
        SessionID sessionID = new SessionID(sessionId);
        NewOrderSingle order = new NewOrderSingle(new ClOrdID("id"), new Side(Side.BUY), new TransactTime(), new OrdType(OrdType.LIMIT));
        order.set(new Price(120.11));
        order.set(new OrderQty(1000));
        order.set(new Symbol("BNA"));
        order.set(new HandlInst('1'));

        Session.sendToTarget(order, sessionID);
    }
}
