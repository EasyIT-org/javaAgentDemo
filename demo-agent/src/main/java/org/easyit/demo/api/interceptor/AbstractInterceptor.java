package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.Context;

public abstract class AbstractInterceptor implements Interceptor {

    protected final CutPoint cutPoint;


    public AbstractInterceptor(CutPoint cutPoint) {
        this.cutPoint = cutPoint;
    }

    protected Context buildContext() {
        return new Context(getCutPoint());
    }

    @Override
    public CutPoint getCutPoint() {
        return cutPoint;
    }
}
