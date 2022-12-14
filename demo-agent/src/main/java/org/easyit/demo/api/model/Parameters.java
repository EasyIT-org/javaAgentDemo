package org.easyit.demo.api.model;

import org.easyit.demo.api.CutPoint;

import java.lang.reflect.Method;

public class Parameters {
    protected BaseParameters baseParameters;


    public Parameters(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, CutPoint cutPoint) {
        this.baseParameters = new BaseParameters(obj, method, allArguments, parameterTypes, cutPoint);
    }

    public Parameters(BaseParameters baseParameters) {
        this.baseParameters = baseParameters;
    }


    public Object getObj() {
        return baseParameters.getObj();
    }

    public Method getMethod() {
        return baseParameters.getMethod();
    }

    public Object[] getAllArguments() {
        return baseParameters.getAllArguments();
    }

    public Class<?>[] getParameterTypes() {
        return baseParameters.getParameterTypes();
    }

}
