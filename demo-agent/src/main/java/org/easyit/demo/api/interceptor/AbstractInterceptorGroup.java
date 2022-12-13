package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.Interceptor;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractInterceptorGroup implements Interceptor {

    @Override
    public void beforeMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        for (Interceptor interceptor : getInterceptorList(obj, method, allArguments, parameterTypes)) {
            interceptor.beforeMethod(obj, method, allArguments, parameterTypes);
        }
    }


    @Override
    public void handleMethodException(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        for (Interceptor interceptor : getInterceptorList(obj, method, allArguments, parameterTypes)) {
            interceptor.handleMethodException(obj, method, allArguments, parameterTypes, t);
        }
    }

    @Override
    public void afterMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        List<Interceptor> interceptorList = getInterceptorList(obj, method, allArguments, parameterTypes);
        for (int i = interceptorList.size() - 1; i >= 0; i--) {
            interceptorList.get(i).afterMethod(obj, method, allArguments, parameterTypes, ret);
        }
    }

    protected abstract List<Interceptor> getInterceptorList(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes);
}
