package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.ComparableInterceptor;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.List;

public abstract class AbstractInterceptorGroup implements Interceptor {

    @Override
    public void beforeMethod(Parameters parameters) {
        for (Interceptor interceptor : getInterceptorList(parameters)) {
            interceptor.beforeMethod(parameters);
        }
    }


    @Override
    public void handleMethodException(ExceptionParameters exceptionParameters) {
        for (Interceptor interceptor : getInterceptorList(exceptionParameters)) {
            interceptor.handleMethodException(exceptionParameters);
        }
    }

    @Override
    public void afterMethod(ReturnParameters returnParameters) {
        List<ComparableInterceptor> interceptorList = getInterceptorList(returnParameters);
        for (int i = interceptorList.size() - 1; i >= 0; i--) {
            interceptorList.get(i).afterMethod(returnParameters);
        }
    }

    protected abstract List<ComparableInterceptor> getInterceptorList(Parameters parameters);
}
