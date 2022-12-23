package org.easyit.demo.api.interceptor;

import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.TracerEndpoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;
import org.easyit.demo.util.ChildrenThreadCount;

import java.util.ArrayList;

public class TraceInterceptor extends AbstractInterceptor {


    public TraceInterceptor(CutPoint cutPoint, ArrayList<TracerEndpoint> tracerAdaptors) {
        super(cutPoint, tracerAdaptors);
    }

    @Override
    protected void doBeforeMethod(TracerEndpoint tracerAdaptor, Parameters parameters, Context context) {
        if (!ChildrenThreadCount.ifRootTraceExist()) {
            // start transaction
            tracerAdaptor.onEnterStart(parameters, context);
            ChildrenThreadCount.rootTraceExist();
        }
        tracerAdaptor.onTraceStart(parameters, context);
    }


    @Override
    protected void doAfterMethod(TracerEndpoint tracerAdaptor, ReturnParameters returnParameters, Context context) {
        tracerAdaptor.onTraceEnd(returnParameters, context);
        int childThreadCount = ChildrenThreadCount.getChildThreadCount();
        if (childThreadCount == 0) { // todo  子线程全部结束了, 且当前线程 trace 除了root 都结束了, 然后,需要在子线程退出时(能不能检查子线程),检查
            tracerAdaptor.onEnterEnd(returnParameters, context);
        } else if (childThreadCount < 0) {
            throw new RuntimeException(); // todo 这个异常需要在这个类里处理掉
        }
    }

    @Override
    protected void doHandleMethodException(TracerEndpoint tracerAdaptor, ExceptionParameters exceptionParameters, Context context) {
        tracerAdaptor.onException(exceptionParameters, context);
    }
}
