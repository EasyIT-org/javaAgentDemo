package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.interceptor.AbstractInterceptorGroup;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MultiMethodInterceptorGroup extends AbstractInterceptorGroup {
    private final Map<String, List<Interceptor>> methodInterceptorMap;

    public MultiMethodInterceptorGroup(Map<String, List<Interceptor>> map) {
        super();
        methodInterceptorMap = map;
    }

    @Override
    protected List<Interceptor> getInterceptorList(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        String name = method.getName();
        List<Interceptor> interceptors = methodInterceptorMap.get(name);
        if (interceptors == null) {
            return Collections.emptyList();
        }
        return interceptors;
    }
}
