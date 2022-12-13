package org.easyit.demo.api.interceptor;

import com.alipay.sofa.common.profile.diagnostic.Profiler;
import org.easyit.demo.api.Interceptor;

import java.lang.reflect.Method;

/**
 * profile entry interceptor
 */
public class EntryInterceptor implements Interceptor {

    private String name;
    private String typeName;

    public EntryInterceptor(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    @Override
    public void beforeMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        resetThreadLocal();
        startProfile();
    }

    private void startProfile() {
        Profiler.start(name);
    }

    private void resetThreadLocal() {
        Profiler.reset();
    }

    @Override
    public void handleMethodException(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {

    }

    @Override
    public void afterMethod(Object obj, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        Profiler.release();
        String dump = Profiler.dump(typeName);
        System.out.println(dump);
    }
}
