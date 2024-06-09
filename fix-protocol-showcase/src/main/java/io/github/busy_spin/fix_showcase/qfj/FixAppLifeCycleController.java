package io.github.busy_spin.fix_showcase.qfj;

public interface FixAppLifeCycleController {

    void start();

    void stop();

    void restart();

    void logout(String sessionId);

    void testRequest(String sessionId, String reqId);

    void login(String sessionId);

    void sendOrder(String sessionId);

    void printLog(String sessionId);

    void setNextNumIn(String sessionId, int number);

    void setNextNumOut(String sessionId, int number);

    void printSessionIds();

    void printSessionDetails(String sessionId);

    String defaultSession();
}
