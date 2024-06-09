package io.github.busy_spin.fix_showcase.qfj;

import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueuingLogFactory;
import io.github.busy_spin.fix_showcase.qfj.utils.store.DefaultBaseDirFileStoreFactory;
import quickfix.*;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFixAppLifeCycleController implements FixAppLifeCycleController {

    private Connector connector;

    private DefaultBaseDirFileStoreFactory messageStoreFactory;

    private final Lock startUpLock = new ReentrantLock();

    private final QueuingLogFactory logFactory = new QueuingLogFactory();

    private final ShellPrinter shellPrinter = new ShellPrinter();

    private SessionSettings sessionSettings;

    private volatile boolean started = false;

    public DefaultFixAppLifeCycleController(AppType appType) {
        init(appType);
    }

    private void init(AppType appType) {
        try {
            if (appType == AppType.INITIATOR) {
                sessionSettings = new SessionSettings(FileUtil.open(ShellPrinter.class, "initiator.cfg"));
                messageStoreFactory = new DefaultBaseDirFileStoreFactory(sessionSettings);
                connector = new SocketInitiator(
                        new Application(),
                        messageStoreFactory,
                        sessionSettings,
                        logFactory,
                        new DefaultMessageFactory());
            } else {
                sessionSettings = new SessionSettings(FileUtil.open(ShellPrinter.class, "acceptor.cfg"));
                messageStoreFactory = new DefaultBaseDirFileStoreFactory(sessionSettings);
                connector = new SocketAcceptor(
                        new Application(),
                        messageStoreFactory,
                        sessionSettings,
                        logFactory,
                        new DefaultMessageFactory());
            }

        } catch (ConfigError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        try {
            startUpLock.lock();
            if (!started) {
                connector.start();
                started = true;
            }
        } catch (ConfigError e) {
            throw new RuntimeException(e);
        } finally {
            startUpLock.unlock();
        }
    }

    @Override
    public void stop() {
        try {
            startUpLock.lock();
            if (started) {
                connector.stop(true);
                started = false;
            }
        } finally {
            startUpLock.unlock();
        }
    }

    @Override
    public void restart() {
        try {
            startUpLock.lock();
            if (started) {
                connector.stop(true);
                started = false;
                try {
                    connector.start();
                    started = true;
                } catch (ConfigError e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            startUpLock.unlock();
        }
    }

    @Override
    public void logout(String sessionId) {
        if (started) {
            try (Session session = Session.lookupSession(new SessionID(sessionId))) {
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
            try (Session session = Session.lookupSession(new SessionID(sessionId))) {
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
            try (Session session = Session.lookupSession(new SessionID(sessionId))) {
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
    public void printLog(String sessionId) {
        shellPrinter.printLogs(logFactory);
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
        shellPrinter.printSessions(sessionSettings);
    }

    @Override
    public void printSessionDetails(String sessionId) {
        try {
            shellPrinter.printSessionDetails(sessionSettings, sessionId);
        } catch (Exception e) {
            System.out.println("Error occurred while reading session config");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String defaultSession() {
        return sessionSettings.sectionIterator().next().toString();
    }

    @Override
    public void printSequenceNumbers(String sessionId) {
        SessionID sessionID = new SessionID(sessionId);
        System.out.println("Session " + sessionID);
        int nextNumIn = messageStoreFactory.getNextNumIn(sessionID);
        int nextNumOut = messageStoreFactory.getNextNumOut(sessionID);

        shellPrinter.printSequenceNumbers(nextNumIn, nextNumOut);
    }


}
