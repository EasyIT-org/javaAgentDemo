package org.easyit.demo.util;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Id {

    private static final String PROCESS_ID = UUID.randomUUID().toString().replaceAll("-", "");
    private static final ThreadLocal<IDContext> THREAD_ID_SEQUENCE = ThreadLocal.withInitial(
            () -> new IDContext(System.currentTimeMillis(), (short) 0));

    private final String id;
    private final AtomicInteger counter;
    private final List<Runnable> callbacks;

    public Id() {
        id = String.join(
                ".",
                PROCESS_ID,
                String.valueOf(Thread.currentThread().getId()),
                String.valueOf(THREAD_ID_SEQUENCE.get().nextSeq())
        );
        counter = new AtomicInteger();
        callbacks = new LinkedList<>();
    }

    public void addCallback(Runnable runnable) {
        callbacks.add(runnable);
    }

    public int increment() {
        return counter.incrementAndGet();
    }

    public int decrement() {
        int i = counter.decrementAndGet();
        if (i == 0) {
            for (Runnable callback : callbacks) {
                try {
                    callback.run();
                } catch (Throwable t) {
                    // todo
                    t.printStackTrace();
                }
            }
        }
        return i;
    }

    @Override
    public String toString() {
        return id;
    }


    private static class IDContext {
        private long lastTimestamp;
        private short threadSeq;

        // Just for considering time-shift-back only.
        private long lastShiftTimestamp;
        private int lastShiftValue;

        private IDContext(long lastTimestamp, short threadSeq) {
            this.lastTimestamp = lastTimestamp;
            this.threadSeq = threadSeq;
        }

        private long nextSeq() {
            return timestamp() * 10000 + nextThreadSeq();
        }

        private long timestamp() {
            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis < lastTimestamp) {
                // Just for considering time-shift-back by Ops or OS. @hanahmily 's suggestion.
                if (lastShiftTimestamp != currentTimeMillis) {
                    lastShiftValue++;
                    lastShiftTimestamp = currentTimeMillis;
                }
                return lastShiftValue;
            } else {
                lastTimestamp = currentTimeMillis;
                return lastTimestamp;
            }
        }

        private short nextThreadSeq() {
            if (threadSeq == 10000) {
                threadSeq = 0;
            }
            return threadSeq++;
        }
    }

}
