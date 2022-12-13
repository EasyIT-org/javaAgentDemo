package org.easyit.demo.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.*;
import org.easyit.demo.api.Interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class CommonInterceptorAdaptor {


    private Interceptor interceptor;

    public CommonInterceptorAdaptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    // todo 看看this 代表什么
    @RuntimeType
    public Object intercept(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> zuper,
                            @Origin Method method) throws Throwable {


        try {
            interceptor.beforeMethod(obj, method, allArguments, method.getParameterTypes());
        } catch (Throwable t) {
//            LOGGER.error(t, "class[{}] before method[{}] intercept failure", obj.getClass(), method.getName());
        }

        Object ret = null;
        try {
//            if (!result.isContinue()) {
//                ret = result._ret();
//            } else {
//                ret = zuper.call();
//            }
            ret = zuper.call();
        } catch (Throwable t) {
            try {
                interceptor.handleMethodException(obj, method, allArguments, method.getParameterTypes(), t);
            } catch (Throwable t2) {
//                LOGGER.error(t2, "class[{}] handle method[{}] exception failure", obj.getClass(), method.getName());
            }
            throw t;
        } finally {
            try {
                interceptor.afterMethod(obj, method, allArguments, method.getParameterTypes(), ret);
            } catch (Throwable t) {
//                LOGGER.error(t, "class[{}] after method[{}] intercept failure", obj.getClass(), method.getName());
            }
        }
        return ret;
    }
}
