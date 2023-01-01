package org.easyit.demo.api.model;

import org.easyit.demo.api.Interceptor;

public interface ComparableInterceptor extends Interceptor, Comparable<ComparableInterceptor> {
    default int compareTo(ComparableInterceptor another) {
        return this.getOrder().compareTo(another.getOrder());
    }

    default Integer getOrder() {
        return 0;
    }
}
