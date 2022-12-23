package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.TracerEndpoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.Collections;
import java.util.List;

public abstract class AbstractInterceptor implements Interceptor {

    protected final CutPoint cutPoint;
    protected final List<TracerEndpoint> tracerEndpoints;


    public AbstractInterceptor(CutPoint cutPoint, List<TracerEndpoint> tracerAdaptors) {
        this.cutPoint = cutPoint;
        this.tracerEndpoints = Collections.unmodifiableList(tracerAdaptors);
    }

    @Override
    public void beforeMethod(Parameters parameters) {
        Context context = buildContext(cutPoint);
        for (TracerEndpoint tracerAdaptor : tracerEndpoints) {
            doBeforeMethod(tracerAdaptor, parameters, context);
        }
    }


    @Override
    public void handleMethodException(ExceptionParameters exceptionParameters) {
        Context context = buildContext(cutPoint);
        for (TracerEndpoint tracerAdaptor : tracerEndpoints) {
            doHandleMethodException(tracerAdaptor, exceptionParameters, context);
        }
    }

    @Override
    public void afterMethod(ReturnParameters returnParameters) {
        Context context = buildContext(cutPoint);
        for (int i = tracerEndpoints.size() - 1; i >= 0; i--) {
            doAfterMethod(tracerEndpoints.get(i), returnParameters, context);
        }
    }

    private Context buildContext(CutPoint cutPoint) {
        return new Context(cutPoint);
    }

    protected abstract void doHandleMethodException(TracerEndpoint tracerAdaptor, ExceptionParameters exceptionParameters, Context context);

    protected abstract void doBeforeMethod(TracerEndpoint tracerAdaptor, Parameters parameters, Context context);

    protected abstract void doAfterMethod(TracerEndpoint tracerAdaptor, ReturnParameters returnParameters, Context context);


    @Override
    public CutPoint getCutPoint() {
        return cutPoint;
    }
}
