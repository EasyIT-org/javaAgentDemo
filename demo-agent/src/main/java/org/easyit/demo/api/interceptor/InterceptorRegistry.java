package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InterceptorRegistry {

    public static final InterceptorRegistry INSTANCE = new InterceptorRegistry();

    public Map<String, Object> interceptorMap = new ConcurrentHashMap<>();

    public Interceptor newInterceptor(CutPoint cp) {
        String type = cp.getType();
        if ("START".equals(type)) {
            return new EntryInterceptor(getNameOrMethodName(cp), "temp.typeName(todo: this property needs load" +
                    " by extractor)");
        } else if ("TRACE".equals(type)) {
            return new TraceInterceptor(getNameOrMethodName(cp));
        }
        throw new IllegalArgumentException("Unexpected type:" + type);
    }

    private static String getNameOrMethodName(CutPoint cp) {
        return cp.getName() == null ? cp.getMethodName() : cp.getName();
    }
}
