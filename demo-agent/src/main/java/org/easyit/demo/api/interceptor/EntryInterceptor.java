package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.TracerAdaptor;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.List;

/**
 * profile entry interceptor
 */
public class EntryInterceptor extends AbstractInterceptor {

    public EntryInterceptor(CutPoint cutPoint, List<TracerAdaptor> tracerAdaptors) {
        super(cutPoint, tracerAdaptors);
    }

    @Override
    protected void doBeforeMethod(TracerAdaptor tracerAdaptor, Parameters parameters, Context context) {
        tracerAdaptor.onEnterStart(parameters, context);
    }

    @Override
    protected void doAfterMethod(TracerAdaptor tracerAdaptor, ReturnParameters returnParameters, Context context) {
        tracerAdaptor.onEnterEnd(returnParameters, context);
    }

    @Override
    protected void doHandleMethodException(TracerAdaptor tracerAdaptor, ExceptionParameters exceptionParameters, Context context) {
        tracerAdaptor.onException(exceptionParameters, context);
    }

    @Override
    public CutPoint getCutPoint() {
        return cutPoint;
    }
}
