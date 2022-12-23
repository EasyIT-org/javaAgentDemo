package org.easyit.demo.api;

import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

public interface Interceptor extends Comparable<Interceptor> {
    void beforeMethod(Parameters parameters);

    void handleMethodException(ExceptionParameters exceptionParameters);

    void afterMethod(ReturnParameters returnParameters);

    default int compareTo(Interceptor another) {
        return this.getOrder().compareTo(another.getOrder());
    }

    default Integer getOrder() {
        return 0;
    }

    CutPoint getCutPoint();
}
