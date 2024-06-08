package io.github.busy_spin.fix_showcase.qfj;

import io.github.busy_spin.fix_showcase.qfj.utils.logs.QueuingLogFactory;
import io.github.busy_spin.fix_showcase.qfj.utils.store.DefaultBaseDirFileStoreFactory;
import quickfix.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class DefaultInitiatorController implements InitiatorController {

    private SocketInitiator socketInitiator;

    private DefaultBaseDirFileStoreFactory messageStoreFactory;

    private Lock initiatorLock = new ReentrantLock();

    private QueuingLogFactory logFactory = new QueuingLogFactory();

    private ShellOutPutHelper shellOutPutHelper = new ShellOutPutHelper();

    private volatile boolean started = false;


    @Override
    public void init() {
        try {
            SessionSettings sessionSettings =
                    new SessionSettings(FileUtil.open(ShellOutPutHelper.class, "initiator.cfg"));
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
                started = true;
                socketInitiator.start();
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
                    e.printStackTrace();
                }
            }
        } finally {
            initiatorLock.unlock();
        }
    }

    @Override
    public void logout(String sessionId) {

    }

    @Override
    public void testRequest(String sessionId) {

    }

    @Override
    public void login(String sessionId) {

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

    }

    @Override
    public void setNextNumOut(String sessionId, int number) {

    }

    @Override
    public void printSessionIds() {
        shellOutPutHelper.printSessions(socketInitiator);
    }


}
