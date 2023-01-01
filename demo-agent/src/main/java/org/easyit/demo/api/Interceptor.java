package org.easyit.demo.api;

import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

public interface Interceptor{
    void beforeMethod(Parameters parameters);

    void handleMethodException(ExceptionParameters exceptionParameters);

    void afterMethod(ReturnParameters returnParameters);

    CutPoint getCutPoint();
}
