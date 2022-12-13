package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.Interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestInterceptor implements Interceptor {

    public static List<String> RECORD = new CopyOnWriteArrayList<>();
    public static final String BEFORE = "beforeMethod";
    public static final String EXCEPTION = "handleMethodException";
    public static final String AFTER = "afterMethod";
    private final int order;
    private final String name;

    public TestInterceptor(int order, String name) {
        this.order = order;
        this.name = name;
    }

    public static void reset() {
        RECORD.clear();
    }

    @Override
    public void beforeMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        RECORD.add(name + BEFORE);
    }

    @Override
    public void handleMethodException(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        RECORD.add(name + EXCEPTION);
    }

    @Override
    public void afterMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        RECORD.add(name + AFTER);
    }

    @Override
    public Integer getOrder() {
        return order;
    }
}
