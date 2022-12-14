package org.easyit.demo.api.model;

import org.easyit.demo.api.CutPoint;

import java.awt.*;
import java.lang.reflect.Method;

public class BaseParameters {

    private final Object obj;
    private final Method method;
    private final Object[] allArguments;
    private final Class<?>[] parameterTypes;

    protected BaseParameters(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, CutPoint cutPoint) {
        this.obj = obj;
        this.method = method;
        this.allArguments = allArguments;
        this.parameterTypes = parameterTypes;
    }

    protected BaseParameters(BaseParameters b) {
        this.obj = b.obj;
        this.method = b.method;
        this.allArguments = b.allArguments;
        this.parameterTypes = b.parameterTypes;
    }

    public Object getObj() {
        return obj;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getAllArguments() {
        return allArguments;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }


}
