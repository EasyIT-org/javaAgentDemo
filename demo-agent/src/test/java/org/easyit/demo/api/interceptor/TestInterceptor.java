package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.BaseParameters;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestInterceptor implements Interceptor {

    public static final String BEFORE = "beforeMethod";
    public static final String EXCEPTION = "handleMethodException";
    public static final String AFTER = "afterMethod";
    public static List<String> RECORD = new CopyOnWriteArrayList<>();
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
    public void beforeMethod(Parameters parameters) {
        RECORD.add(name + BEFORE);
    }

    @Override
    public void handleMethodException(ExceptionParameters exceptionParameters) {
        RECORD.add(name + EXCEPTION);
    }

    @Override
    public void afterMethod(ReturnParameters returnParameters) {
        RECORD.add(name + AFTER);
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public CutPoint getCutPoint() {
        return null;
    }
}
