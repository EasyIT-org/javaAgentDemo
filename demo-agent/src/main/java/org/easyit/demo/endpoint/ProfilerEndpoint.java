package org.easyit.demo.endpoint;


import com.alibaba.ttl.TransmittableThreadLocal;
import org.easyit.demo.api.Endpoint;
import org.easyit.demo.api.model.*;
import org.easyit.demo.util.Profiler;

// TODO: 2022/12/15 确保不能抛出任何异常
public enum ProfilerEndpoint implements Endpoint {
    INSTANCE; // todo 不应该用单例的?
    TransmittableThreadLocal<TraceState> childrenThreadCount;

    ProfilerEndpoint() {
        childrenThreadCount = new TransmittableThreadLocal<>();
    }

    private void beforeExecute() {

    }

    private void afterExecute() {
        // TODO: 2022/12/14 经过判断条件,外层已经没有 entry ,且外
        //  层非 FIRST entry  release ,就可以输出了
        String dump = Profiler.dump();
        System.out.println(dump);
    }

    @Override
    public String getName() {
        return "Profiler";
    }

    @Override
    public void onTaskBuild() {

    }
    @Override
    public void onTaskStart() {

    }
    @Override
    public void onTaskEnd() {

    }

    @Override
    public void onException(ExceptionParameters exceptionParameters, Context context) {

    }

    @Override
    public void onTraceStart(Parameters parameters, Context context) {
        String name = context.getCutPoint().getName();
        Profiler.enter(name);
    }

    @Override
    public void onTraceEnd(ReturnParameters returnParameters, Context context) {

    }

}
