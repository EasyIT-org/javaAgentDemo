package org.easyit.demo.endpoint;

import org.easyit.demo.api.TracerEndpoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

public enum EndpointManager implements TracerEndpoint {
    INSTANCE;

    @Override
    public void onEnterStart(Parameters parameters, Context context) {

    }

    @Override
    public void onEnterEnd(ReturnParameters returnParameters, Context context) {

    }

    @Override
    public void onException(ExceptionParameters exceptionParameters, Context context) {

    }

    @Override
    public void onTraceStart(Parameters parameters, Context context) {

    }

    @Override
    public void onTraceEnd(ReturnParameters returnParameters, Context context) {

    }
}
