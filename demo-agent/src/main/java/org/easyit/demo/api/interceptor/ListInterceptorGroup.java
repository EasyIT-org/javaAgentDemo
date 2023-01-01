package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.ComparableInterceptor;
import org.easyit.demo.api.model.Parameters;

import java.util.Collections;
import java.util.List;

public class ListInterceptorGroup extends AbstractInterceptorGroup {

    private final List<ComparableInterceptor> interceptorList;

    public ListInterceptorGroup(List<ComparableInterceptor> interceptorList) {
        for (Interceptor interceptor : interceptorList) {
            if (interceptor == null) {
                throw new NullPointerException("Interceptor can not be null");
            }
        }
        Collections.sort(interceptorList);
        this.interceptorList = Collections.unmodifiableList(interceptorList);

    }

    @Override
    protected List<ComparableInterceptor> getInterceptorList(Parameters parameters) {
        return interceptorList;
    }

    @Override
    public CutPoint getCutPoint() {
        // TODO: 2022/12/13  
        return null;
    }
}
