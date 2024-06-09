package io.github.busy_spin.fix_showcase.qfj;

import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueuingLogFactory;
import io.github.busy_spin.fix_showcase.qfj.utils.store.DefaultBaseDirFileStoreFactory;
import quickfix.*;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultInitiatorController implements InitiatorController {

    private SocketInitiator socketInitiator;

    private DefaultBaseDirFileStoreFactory messageStoreFactory;

    private Lock initiatorLock = new ReentrantLock();

    private QueuingLogFactory logFactory = new QueuingLogFactory();

    private ShellOutPutHelper shellOutPutHelper = new ShellOutPutHelper();

    private SessionSettings sessionSettings;

    private volatile boolean started = false;


    @Override
    public void init() {
        try {
            sessionSettings = new SessionSettings(FileUtil.open(ShellOutPutHelper.class, "initiator.cfg"));
            messageStoreFactory = new DefaultBaseDirFileStoreFactory(sessionSettings);
            socketInitiator = new SocketInitiator(
                    new Application(),
                    messageStoreFactory,
                    sessionSettings,
                    logFactory,
                    new DefaultMessageFactory());
        } catch (ConfigError e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            initiatorLock.lock();
            if (!started) {
                if (socketInitiator == null) {
                    init();
                }
                socketInitiator.start();
                started = true;
            }
        } catch (ConfigError e) {
            e.printStackTrace();
        } finally {
            initiatorLock.unlock();
        }
    }

    @Override
    public void stop() {
        try {
            initiatorLock.lock();
            if (started) {
                socketInitiator.stop(true);
                started = false;
            }
        } finally {
            initiatorLock.unlock();
        }
    }

    @Override
    public void restart() {
        try {
            initiatorLock.lock();
            if (started) {
                socketInitiator.stop(true);
                started = false;
                try {
                    socketInitiator.start();
                    started = true;
                } catch (ConfigError e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            initiatorLock.unlock();
        }
    }

    @Override
    public void logout(String sessionId) {
        if (started) {
            try(Session session = Session.lookupSession(new SessionID(sessionId))) {
                session.logout();
                System.out.println("Logout from session " + sessionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Initiator is not running");
        }
    }

    @Override
    public void testRequest(String sessionId, String reqId) {
        if (started) {
            try(Session session = Session.lookupSession(new SessionID(sessionId))) {
                if (reqId == null) {
                    reqId = String.valueOf(System.nanoTime());
                }
                session.generateTestRequest(reqId);
                System.out.printf("Sending test request with req-id %s to %s\n", reqId, sessionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Initiator is not running");
        }
    }

    @Override
    public void login(String sessionId) {
        if (started) {
            try(Session session = Session.lookupSession(new SessionID(sessionId))) {
                session.logon();
                System.out.println("Logon to session " + sessionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Initiator is not running");
        }
    }

    @Override
    public void sendOrder(String sessionId) {

    }

    @Override
    public void printLog(String sessionId) {
        shellOutPutHelper.printLogs(logFactory);
    }

    @Override
    public void setNextNumIn(String sessionId, int number) {
        int currentNextNumIn = messageStoreFactory.setNextNumIn(new SessionID(sessionId), number);
        System.out.printf("Next Num In changed from %d to %d\n", currentNextNumIn, number);
    }

    @Override
    public void setNextNumOut(String sessionId, int number) {
        int currentNextNumOut = messageStoreFactory.setNextNumOut(new SessionID(sessionId), number);
        System.out.printf("Next Num Out changed from %d to %d\n", currentNextNumOut, number);
    }

    @Override
    public void printSessionIds() {
        shellOutPutHelper.printSessions(sessionSettings);
    }

    @Override
    public void printSessionDetails(String sessionId) {
        try {
            shellOutPutHelper.printSessionDetails(sessionSettings, sessionId);
        } catch (Exception e) {
            System.out.println("Error occurred while reading session config");
            throw new RuntimeException(e);
        }

    }

    @Override
    public String defaultSession() {
        return sessionSettings.sectionIterator().next().toString();
    }


}
