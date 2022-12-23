package org.easyit.demo.api;

import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;


public interface TracerEndpoint {

    void onEnterStart(Parameters parameters, Context context);

    void onEnterEnd(ReturnParameters returnParameters, Context context);

    void onException(ExceptionParameters exceptionParameters, Context context);

    void onTraceStart(Parameters parameters, Context context);

    void onTraceEnd(ReturnParameters returnParameters, Context context);


}
