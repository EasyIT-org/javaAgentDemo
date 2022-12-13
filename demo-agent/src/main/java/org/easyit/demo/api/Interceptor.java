package org.easyit.demo.api;

import java.lang.reflect.Method;

public interface Interceptor extends Comparable<Interceptor> {
    void beforeMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes);

    void handleMethodException(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t);

    void afterMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret);

    default int compareTo(Interceptor another) {
        return this.getOrder().compareTo(another.getOrder());
    }

    default Integer getOrder() {
        return 0;
    }
}
