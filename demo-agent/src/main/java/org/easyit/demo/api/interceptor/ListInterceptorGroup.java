package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.Parameters;

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
//        Collections.sort(interceptorList);
// todo if interceptor need compare?
        this.interceptorList = Collections.unmodifiableList(interceptorList);

    }

    @Override
    protected List<Interceptor> getInterceptorList(Parameters parameters) {
        return interceptorList;
    }

    @Override
    public CutPoint getCutPoint() {
        return interceptorList.get(0).getCutPoint();
    }
}
