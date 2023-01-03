package org.easyit.demo.boot;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.interceptor.InterceptorRegistry;
import org.easyit.demo.api.interceptor.ListInterceptorGroup;
import org.easyit.demo.bytebuddy.ByteBuddyInterceptorAdaptor;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ByteBuddyStarter {

    public void start(Instrumentation instrumentation) {
        List<CutPoint> cutPoints = loadCutPoints();
        Map<String, Map<String, List<CutPoint>>> cutPointMap = parseCutPoints(cutPoints);


        AgentBuilder.Identified.Extendable extendable = new AgentBuilder.Default().with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(multiNameMatcher(cutPointMap.keySet()))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, ProtectionDomain protectionDomain) {

                        // todo 现在这里的 类-方法-interceptor 的关系是 1:N:1 ,在interceptor中又存放一个 map 来确定最终的 interceptor
                        // todo 需要考虑下,是否把这种关系改为 1:N:N,在构造之初,就把方法对应的 interceptor 确定下来
                        Map<String, List<CutPoint>> methodMap = cutPointMap.get(typeDescription.getName()); // todo debug 看下这边的 typeDescription.getName()
                        DynamicType.Builder<?> tmp = builder;
                        for (Map.Entry<String, List<CutPoint>> methodCutPointEntry : methodMap.entrySet()) {
                            tmp = tmp.method(named(methodCutPointEntry.getKey()))
                                    .intercept(MethodDelegation.withDefaultConfiguration().to(getInterceptorByCutPoint(methodCutPointEntry.getValue())));
                        }
                        return tmp;
                    }
                });
        extendable.installOn(instrumentation);
    }

    private ByteBuddyInterceptorAdaptor getInterceptorByCutPoint(List<CutPoint> cutPoints) {
        List<Interceptor> interceptorList = cutPoint2Interceptor(cutPoints);
        ListInterceptorGroup listInterceptorGroup = new ListInterceptorGroup(interceptorList);
        return new ByteBuddyInterceptorAdaptor(listInterceptorGroup);
    }

    private Map<String, Map<String, List<CutPoint>>> parseCutPoints(List<CutPoint> cutPoints) {
        return cutPoints.stream().collect(Collectors.groupingBy(CutPoint::getClassName, Collectors.groupingBy(CutPoint::getMethodName)));


    }

    private List<CutPoint> loadCutPoints() {
        // todo read from file
        CutPoint.CutPointImpl cutPoint1 = new CutPoint.CutPointImpl("SOFARPC.startRPC", "START", "com.alipay.sofa.rpc.server.bolt.BoltServerProcessor", "handleRequest");
        CutPoint.CutPointImpl cutPoint2 = new CutPoint.CutPointImpl("SOFARPC.startBiz", "TRACE", "com.alipay.sofa.rpc.server.bolt.BoltServerProcessor", "handleRequest");
        List<CutPoint> cutPoints = new ArrayList<>();
        cutPoints.add(cutPoint1);
        cutPoints.add(cutPoint2);
        return cutPoints;
    }

    private List<Interceptor> cutPoint2Interceptor(List<CutPoint> cutPoints) {
        return cutPoints.stream().map(InterceptorRegistry.INSTANCE::newInterceptor).collect(Collectors.toList());
    }

    private ElementMatcher<TypeDescription> multiNameMatcher(Collection<String> typeNames) {
        ElementMatcher<TypeDescription> matcher = ElementMatchers.none();
        for (String typeName : typeNames) {
            matcher = named(typeName).or(matcher);
        }
        return matcher;
    }

}
