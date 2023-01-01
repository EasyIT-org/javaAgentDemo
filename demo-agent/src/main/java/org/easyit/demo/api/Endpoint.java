package org.easyit.demo.api;

import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;


public interface Endpoint {

    String getName();

    void onTaskBuild();

    void onTaskStart();

    void onTaskEnd();

    void onTraceStart(Parameters parameters, Context context);

    void onTraceEnd(ReturnParameters returnParameters, Context context);

    void onException(ExceptionParameters exceptionParameters, Context context);

}
