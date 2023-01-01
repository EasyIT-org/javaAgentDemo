package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.Endpoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.Collections;
import java.util.List;

public abstract class AbstractInterceptor implements Interceptor {

    protected final CutPoint cutPoint;
    protected final List<Endpoint> endpoints;


    public AbstractInterceptor(CutPoint cutPoint, List<Endpoint> tracerAdaptors) {
        this.cutPoint = cutPoint;this.endpoints = Collections.unmodifiableList(tracerAdaptors);
    }

    @Override
    public void beforeMethod(Parameters parameters) {
        Context context = buildContext(cutPoint);
        for (Endpoint tracerAdaptor : endpoints) {
            doBeforeMethod(tracerAdaptor, parameters, context);
        }
    }


    @Override
    public void handleMethodException(ExceptionParameters exceptionParameters) {
        Context context = buildContext(cutPoint);
        for (Endpoint tracerAdaptor : endpoints) {
            doHandleMethodException(tracerAdaptor, exceptionParameters, context);
        }
    }

    @Override
    public void afterMethod(ReturnParameters returnParameters) {
        Context context = buildContext(cutPoint);
        for (int i = endpoints.size() - 1; i >= 0; i--) {
            doAfterMethod(endpoints.get(i), returnParameters, context);
        }
    }

    private Context buildContext(CutPoint cutPoint) {
        return new Context(cutPoint);
    }

    protected abstract void doHandleMethodException(Endpoint tracerAdaptor, ExceptionParameters exceptionParameters, Context context);

    protected abstract void doBeforeMethod(Endpoint tracerAdaptor, Parameters parameters, Context context);

    protected abstract void doAfterMethod(Endpoint tracerAdaptor, ReturnParameters returnParameters, Context context);


    @Override
    public CutPoint getCutPoint() {
        return cutPoint;
    }
}
