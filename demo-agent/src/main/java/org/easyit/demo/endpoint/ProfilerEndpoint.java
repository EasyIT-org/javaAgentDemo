package org.easyit.demo.endpoint;


import com.alibaba.ttl.TransmittableThreadLocal;
import org.easyit.demo.api.TracerEndpoint;
import org.easyit.demo.api.model.*;
import org.easyit.demo.util.CallbackThreadLocal;
import org.easyit.demo.util.Profiler;

// TODO: 2022/12/15 确保不能抛出任何异常
public class ProfilerEndpoint implements TracerEndpoint {

    private static CallbackThreadLocal<Profiler.Entry> threadLocal;
    private static TransmittableThreadLocal<TraceState> childrenThreadCount;

    public ProfilerEndpoint() {
        threadLocal = new CallbackThreadLocal<>(this::beforeExecute, this::afterExecute);
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
    public void onEnterStart(Parameters parameters, Context context) {
        // reset thread local
        // todo 需要确定下, 有没有外围的Profiler
        // todo 看看能不能检测下内存泄漏


        // 如果之前没有任何一个 Entry, 创建一个初始 Entry
        Profiler.Entry all = threadLocal.get();

        // TODO: 2022/12/14 现在Profile 不支持两个Entry 都没 release
        if (all == null) {
            Profiler.start("ALL");
            threadLocal.set(Profiler.getCurrentEntry());
        }

        String name = context.getCutPoint().getName();
        Profiler.enter(name);
    }

    @Override
    public void onEnterEnd(ReturnParameters returnParameters, Context context) {
        Profiler.release();
        //
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
        Profiler.release();
    }
}
