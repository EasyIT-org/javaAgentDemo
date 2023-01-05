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
        // TODO: 2023/1/3
    }


    @Override
    public void onSpanStart(Parameters parameters, Context context) {
        String name = context.getCutPoint().getName();
        Profiler.enter(name);
    }

    @Override
    public void onSpanEnd(ReturnParameters returnParameters, Context context) {
        Profiler.release();
    }

    @Override
    public void onSegmentStart() {
        String currentThreadName = Thread.currentThread().getName();
        Profiler.start("START-" + currentThreadName);
    }

    @Override
    public void onSegmentEnd() {
        Profiler.release();
        String result = Profiler.dump();
        // todo: add some filter
        profilerContainer.add(IdHolder.getTraceId().toString(), IdHolder.getSpanId().toString(), result);
    }

    @Override
    public void onTraceEnd() {
        String traceId = IdHolder.getTraceId().toString();
        Map<String, String> segmentIdMap = profilerContainer.pop(traceId);
        System.out.println("\n--------------------traceId:"+ traceId+"-----------------");
        // TODO: 2023/1/3 print prettily
        for (Map.Entry<String, String> entry : segmentIdMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

    }

    public class ProfilerContainer {
        /**
         * TraceId -> SegmentId -> result
         */
        // todo be careful , OOM
        private final Map<String, Map<String, String>> container;

        public ProfilerContainer() {
            this.container = new ConcurrentHashMap<>();
        }

        public void add(String traceId, String segmentId, String result) {
            Map<String, String> spanIdMap = container.computeIfAbsent(traceId, t -> new ConcurrentHashMap<>());
            spanIdMap.put(segmentId, result);
        }

        public Map<String, String> pop(String traceId) {
            Map<String, String> segmentIdMap = container.remove(traceId);
            return segmentIdMap;
        }


        public void clear() {
            container.clear();
        }
    }

}
