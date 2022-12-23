package org.easyit.demo.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.*;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ByteBuddyInterceptorAdaptor {


    private Interceptor interceptor;

    public ByteBuddyInterceptorAdaptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    // todo 看看this 代表什么
    @RuntimeType
    public Object intercept(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> zuper,
                            @Origin Method method) throws Throwable {

        Parameters baseParameters = new Parameters(obj, method, allArguments, method.getParameterTypes(), interceptor.getCutPoint());
        try {
            interceptor.beforeMethod(baseParameters);
        } catch (Throwable t) {
//            LOGGER.error(t, "class[{}] before method[{}] intercept failure", obj.getClass(), method.getName());
        }

        Object ret = null;
        try {
            ret = zuper.call();
        } catch (Throwable t) {
            try {
                interceptor.handleMethodException(new ExceptionParameters(baseParameters, t));
            } catch (Throwable t2) {
//                LOGGER.error(t2, "class[{}] handle method[{}] exception failure", obj.getClass(), method.getName());
            }
            throw t;
        } finally {
            try {
                interceptor.afterMethod(new ReturnParameters(baseParameters, ret));
            } catch (Throwable t) {
//                LOGGER.error(t, "class[{}] after method[{}] intercept failure", obj.getClass(), method.getName());
            }
        }
        return ret;
    }
}
