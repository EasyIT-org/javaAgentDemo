package org.easyit.demo.adaptor;

import com.alipay.sofa.common.profile.diagnostic.Profiler;
import org.easyit.demo.api.TracerAdaptor;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

public class ProfilerAdaptor implements TracerAdaptor {

    @Override
    public void onEnterStart(Parameters parameters, Context context) {
        // reset thread local
        // todo 需要确定下, 有没有外围的Profiler
        // todo 看看能不能检测下内存泄漏
        Profiler.reset();
        String name = context.getCutPoint().getName();
        Profiler.start(name);
    }

    @Override
    public void onEnterEnd(ReturnParameters returnParameters, Context context) {
        Profiler.release();
        // todo report info to a reporter
        String dump = Profiler.dump();
        System.out.println(dump);
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
