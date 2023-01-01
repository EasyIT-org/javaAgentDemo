package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;

public class InterceptorRegistry {

    public static final InterceptorRegistry INSTANCE = new InterceptorRegistry();

    public Interceptor newInterceptor(CutPoint cp) {

        String type = cp.getType();
        if ("START".equals(type)) {
            return new TraceInterceptor(cp);
        } else if ("TRACE".equals(type)) {
            return new TraceInterceptor(cp);
        }
        throw new IllegalArgumentException("Unexpected type:" + type);
    }
}
