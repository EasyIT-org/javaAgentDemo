package org.easyit.demo.api.interceptor;

import com.alipay.sofa.common.profile.diagnostic.Profiler;
import org.easyit.demo.api.Interceptor;

import java.lang.reflect.Method;

public class TraceInterceptor implements Interceptor {

    private String name;

    public TraceInterceptor(String name) {
        this.name = name;
    }

    @Override
    public void beforeMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        Profiler.enter(name);
    }

    @Override
    public void handleMethodException(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {

    }

    @Override
    public void afterMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        Profiler.release();
    }
}
