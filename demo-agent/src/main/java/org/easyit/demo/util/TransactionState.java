package org.easyit.demo.util;

import org.easyit.demo.event.Event;
import org.easyit.demo.event.EventBus;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. 提供 root 的状态, 是否完成
 */
public class TransactionState {
    private static final ThreadLocal<AtomicInteger> ROOT_TRACE_COUNT = new InheritableThreadLocal<>();
    private static final ThreadLocal<AtomicInteger> CURRENT_TRACE_COUNT = new ThreadLocal<>();

    public static void spanStart() {
        AtomicInteger atomicInteger = CURRENT_TRACE_COUNT.get();
        atomicInteger.incrementAndGet();
    }

    public static void spanEnd() { // todo 这是现有鸡还是现有蛋的问题, 这个类和 确定 transaction 什么时候开始, 什么时候结束有很强的关联性, 需要分析一下

        AtomicInteger atomicInteger = CURRENT_TRACE_COUNT.get();
        atomicInteger.decrementAndGet();
    }

    public static void rootSpanStart() {
        ROOT_TRACE_COUNT.set(CURRENT_TRACE_COUNT.get());
    }

    public static void rootSpanEnd() {

    }


    public static void init() {
        EventBus.register(Event.Default.TransactionEnd, e -> TransactionState.rootSpanStart());
    }


}
