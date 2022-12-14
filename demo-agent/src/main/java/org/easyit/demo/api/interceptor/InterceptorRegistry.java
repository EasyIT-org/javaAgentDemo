package org.easyit.demo.api.interceptor;

import com.google.common.collect.Lists;
import org.easyit.demo.adaptor.ProfilerAdaptor;
import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.TracerAdaptor;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InterceptorRegistry {

    public static final InterceptorRegistry INSTANCE = new InterceptorRegistry();

    public Map<String, Object> interceptorMap = new ConcurrentHashMap<>();
    private ProfilerAdaptor profilerAdaptor = new ProfilerAdaptor();
    private ArrayList<TracerAdaptor> tracerAdaptors = Lists.newArrayList(profilerAdaptor);

    public Interceptor newInterceptor(CutPoint cp) {

        String type = cp.getType();
        if ("START".equals(type)) {
            return new EntryInterceptor(cp, tracerAdaptors);
        } else if ("TRACE".equals(type)) {
            return new TraceInterceptor(cp, tracerAdaptors);
        }
        throw new IllegalArgumentException("Unexpected type:" + type);
    }
}
