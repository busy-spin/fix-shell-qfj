package io.github.busy_spin.fix_showcase.qfj;

import java.util.List;

public interface InitiatorController {
    void init();

    void start();

    void stop();

    void restart();

    void logout(String sessionId);

    void testRequest(String sessionId);

    void login(String sessionId);

    void sendOrder(String sessionId);

    void printLog(String sessionId);

    void setNextNumIn(String sessionId, int number);

    void setNextNumOut(String sessionId, int number);

    void printSessionIds();
}
