package org.easyit.demo.endpoint;


import org.easyit.demo.api.Endpoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;
import org.easyit.demo.util.IdHolder;
import org.easyit.demo.util.Profiler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: 2022/12/15 确保不能抛出任何异常
public enum ProfilerEndpoint implements Endpoint {
    INSTANCE; // todo 不应该用单例的?

    private ProfilerContainer profilerContainer;

    ProfilerEndpoint() {
        profilerContainer = new ProfilerContainer();
    }

    @Override
    public String getName() {
        return "Profiler";
    }

    @Override
    public void onException(ExceptionParameters exceptionParameters, Context context) {

    }

    @Override
    public void report() {
        String result = Profiler.dump();
        // todo: add some filter
        profilerContainer.save(IdHolder.getTraceId().toString(), IdHolder.getSpanId().toString(), result);

    }

    @Override
    public void onTraceStart(Parameters parameters, Context context) {
        String name = context.getCutPoint().getName();
        Profiler.enter(name);
    }

    @Override
    public void onTraceEnd(ReturnParameters returnParameters, Context context) {
        Profiler.release();
    }

    public class ProfilerContainer {
        /**
         * TraceId -> SpanId -> result
         */
        // todo be careful , OOM
        private final Map<String, Map<String, String>> container;

        public ProfilerContainer() {
            this.container = new ConcurrentHashMap<>();
        }

        public void save(String traceId, String spanId, String result) {
            Map<String, String> spanIdMap = container.computeIfAbsent(traceId, t -> new ConcurrentHashMap<>());
            spanIdMap.put(spanId, result);
        }

        public void clear() {
            container.clear();
        }
    }

}
