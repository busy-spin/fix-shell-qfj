package io.github.busy_spin.fix_showcase.qfj.utils;

import lombok.Getter;
import quickfix.Log;

import java.util.concurrent.ArrayBlockingQueue;

public class QueueLog implements Log {

    @Getter
    private final ArrayBlockingQueue<LogElement> queue = new ArrayBlockingQueue<>(500);

    @Override
    public void clear() {
        queue.clear();
    }


    @Override
    public void onIncoming(String s) {
        recordInQueue("INCOMING", s);
    }

    private void recordInQueue(String s, String eventId) {
        while (!queue.offer(new LogElement(eventId, s))) {
            queue.poll();
        }
    }

    @Override
    public void onOutgoing(String s) {
        recordInQueue("OUTGOING", s);
    }

    @Override
    public void onEvent(String s) {
        recordInQueue("EVENT", s);
    }

    @Override
    public void onErrorEvent(String s) {
        recordInQueue("ERROR", s);
    }
}
