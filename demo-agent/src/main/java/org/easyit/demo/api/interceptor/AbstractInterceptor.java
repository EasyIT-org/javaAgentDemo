package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.TracerAdaptor;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.Collections;
import java.util.List;

public abstract class AbstractInterceptor implements Interceptor {

    protected final CutPoint cutPoint;
    protected final List<TracerAdaptor> tracerAdaptors;


    public AbstractInterceptor(CutPoint cutPoint, List<TracerAdaptor> tracerAdaptors) {
        this.cutPoint = cutPoint;
        this.tracerAdaptors = Collections.unmodifiableList(tracerAdaptors);
    }

    @Override
    public void beforeMethod(Parameters parameters) {
        Context context = buildContext(cutPoint);
        for (TracerAdaptor tracerAdaptor : tracerAdaptors) {
            doBeforeMethod(tracerAdaptor, parameters, context);
        }
    }


    @Override
    public void handleMethodException(ExceptionParameters exceptionParameters) {
        Context context = buildContext(cutPoint);
        for (TracerAdaptor tracerAdaptor : tracerAdaptors) {
            doHandleMethodException(tracerAdaptor, exceptionParameters, context);
        }
    }

    @Override
    public void afterMethod(ReturnParameters returnParameters) {
        Context context = buildContext(cutPoint);
        for (int i = tracerAdaptors.size() - 1; i >= 0; i--) {
            doAfterMethod(tracerAdaptors.get(i), returnParameters, context);
        }
    }

    private Context buildContext(CutPoint cutPoint) {
        return new Context(cutPoint);
    }

    protected abstract void doHandleMethodException(TracerAdaptor tracerAdaptor, ExceptionParameters exceptionParameters, Context context);

    protected abstract void doBeforeMethod(TracerAdaptor tracerAdaptor, Parameters parameters, Context context);

    protected abstract void doAfterMethod(TracerAdaptor tracerAdaptor, ReturnParameters returnParameters, Context context);


    @Override
    public CutPoint getCutPoint() {
        return cutPoint;
    }
}
