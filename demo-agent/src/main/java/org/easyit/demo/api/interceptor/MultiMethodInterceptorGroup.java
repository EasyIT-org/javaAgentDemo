package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.Parameters;

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
    protected List getInterceptorList(Parameters parameters) {
        String name = parameters.getMethod().getName();
        List<Interceptor> interceptors = methodInterceptorMap.get(name);
        if (interceptors == null) {
            return Collections.emptyList();
        }
        return interceptors;
    }

    @Override
    public CutPoint getCutPoint() {
        // TODO: 2022/12/13
        return null;
    }
}
