package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;
import org.easyit.demo.endpoint.EndpointManager;
import org.easyit.demo.util.IdHolder;

public class TraceInterceptor extends AbstractInterceptor {

    public TraceInterceptor(CutPoint cutPoint) {
        super(cutPoint);
    }

    @Override
    public void beforeMethod(Parameters parameters) {
        Context context = buildContext();
        IdHolder.start();
        EndpointManager.INSTANCE.onTraceStart(parameters, context);
    }

    @Override
    public void handleMethodException(ExceptionParameters exceptionParameters) {
        Context context = buildContext();
        EndpointManager.INSTANCE.onException(exceptionParameters, context);
    }

    @Override
    public void afterMethod(ReturnParameters returnParameters) {
        Context context = buildContext();
        EndpointManager.INSTANCE.onTraceEnd(returnParameters, context);
        IdHolder.end();
    }
}
