package org.easyit.demo.util;

import com.alibaba.ttl.TransmittableThreadLocal;

public class CallbackThreadLocal<T> extends TransmittableThreadLocal<T> {


    private final Runnable beforeExecute;
    private final Runnable afterExecute;

    public CallbackThreadLocal(Runnable beforeExecute, Runnable afterExecute) {
        this.beforeExecute = beforeExecute;
        this.afterExecute = afterExecute;
    }

    @Override
    protected void beforeExecute() {
        super.beforeExecute();
    }

    @Override
    protected void afterExecute() {
        super.afterExecute();
    }
}
