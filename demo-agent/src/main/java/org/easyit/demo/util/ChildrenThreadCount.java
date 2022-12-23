package org.easyit.demo.util;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.atomic.AtomicInteger;

public class ChildrenThreadCount {

    private static final TransmittableThreadLocal<AtomicInteger> TASK_COUNT = new TransmittableThreadLocal<>();
    private static final TransmittableThreadLocal<Boolean> rootTraceExist = new TransmittableThreadLocal<>();


    private static final TransmittableThreadLocal.Transmitter.Transmittee<Object, Object> childrenThreadCounter = new TransmittableThreadLocal.Transmitter.Transmittee<Object, Object>() {
        @Override
        public Object capture() {
            // 这个方法在父线程中执行, 会在创建 runnable 时执行.
            // todo 这里有个问题: 如果父线程创建了一个runnable ,但是没有执行怎么办? 这就没办法了....  想办法把信息暴露出来吧

            // 需要在存在 root Trace 的情况下, 再计算 ChildrenThreadCount
            if (ifRootTraceExist()) {
                // 拿到并创建 ThreadLocal
                AtomicInteger adder = TASK_COUNT.get();
                if (adder == null) {
                    adder = new AtomicInteger();
                    TASK_COUNT.set(adder);
                }
                adder.incrementAndGet();
            }
            return null;
        }


        @Override
        public Object replay(Object captured) {
            // do nothing;
            return null;
        }


        @Override
        public Object clear() {
            // todo 没理解这个方法有什么用, 应该和 replay 一样, 是用不到的.
            return null;
        }

        @Override
        public void restore(Object backup) {
            AtomicInteger counter = TASK_COUNT.get();
            if (counter != null) {
                int i = counter.decrementAndGet();

                if(i == 0){
                    // When this task is completed, there is no other task is running.
                    // If the root rook task is completed , we can stop this transaction.
                    if(rootTask)


                }else if (i < 0) {
                    // indicate that root thread exists
                    TASK_COUNT.remove();
                    rootTraceExist.set(Boolean.FALSE);
                }
            }

        }
    };

    static {
        TransmittableThreadLocal.Transmitter.registerTransmittee(childrenThreadCounter);
    }

    public static boolean ifRootTraceExist() {
        return rootTraceExist.get() != null && rootTraceExist.get();
    }

    /**
     * @return non-zero if counter exist and returns the count; -1 if counter not exists
     */
    public static int getChildThreadCount() {
        AtomicInteger atomicInteger = TASK_COUNT.get();
        if (atomicInteger != null) {
            return atomicInteger.get();
        }
        return -1;
    }


    public static void rootTraceExist() {
        rootTraceExist.set(Boolean.TRUE);
    }

}
