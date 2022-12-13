package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.interceptor.AbstractInterceptorGroup;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ListInterceptorGroup extends AbstractInterceptorGroup {


    private final List<Interceptor> interceptorList;

    public ListInterceptorGroup(List<Interceptor> interceptorList) {
        for (Interceptor interceptor : interceptorList) {
            if (interceptor == null) {
                throw new NullPointerException("Interceptor can not be null");
            }
        }
        Collections.sort(interceptorList);
        this.interceptorList = Collections.unmodifiableList(interceptorList);

    }

    @Override
    protected List<Interceptor> getInterceptorList(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        return interceptorList;
    }
}
