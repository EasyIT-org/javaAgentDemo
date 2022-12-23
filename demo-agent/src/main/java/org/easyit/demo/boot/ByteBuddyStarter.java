package org.easyit.demo.boot;

import com.google.common.collect.Lists;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.Interceptor;
import org.easyit.demo.api.interceptor.MultiMethodInterceptorGroup;
import org.easyit.demo.api.interceptor.InterceptorRegistry;
import org.easyit.demo.bytebuddy.ByteBuddyInterceptorAdaptor;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
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
                .type(buildTypeMatcher(cutPointMap))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, ProtectionDomain protectionDomain) {
                        return builder
                                .method(buildMethodMatcher(typeDescription, cutPointMap))
                                .intercept(MethodDelegation.withDefaultConfiguration().to(getCommonInterceptor(typeDescription, cutPointMap)));
                    }
                });
        extendable.installOn(instrumentation);
    }

    private Map<String, Map<String, List<CutPoint>>> parseCutPoints(List<CutPoint> cutPoints) {
        return cutPoints.stream().collect(Collectors.groupingBy(CutPoint::getClassName, Collectors.groupingBy(CutPoint::getMethodName)));


    }

    private List<CutPoint> loadCutPoints() {
        CutPoint.CutPointImpl cutPoint1 = new CutPoint.CutPointImpl("SOFARPC.startRPC", "START", "com.alipay.sofa.rpc.server.bolt.BoltServerProcessor", "handleRequest");
        CutPoint.CutPointImpl cutPoint2 = new CutPoint.CutPointImpl("SOFARPC.startBiz", "TRACE", "com.alipay.sofa.rpc.server.bolt.BoltServerProcessor", "handleRequest");
        return Lists.newArrayList(cutPoint1, cutPoint2);


    }

    private ElementMatcher<MethodDescription> buildMethodMatcher(TypeDescription typeDescription, Map<String, Map<String, List<CutPoint>>> map) {
        Map<String, List<CutPoint>> methodMap = map.get(typeDescription.getName()); // todo debug 看下这边的 typeDescription.getName()
        return multiNameMatcher(methodMap.keySet());
    }

    private ByteBuddyInterceptorAdaptor getCommonInterceptor(TypeDescription typeDescription, Map<String, Map<String, List<CutPoint>>> map) {
        Map<String, List<CutPoint>> methodMap = map.get(typeDescription.getName());
        Interceptor multiMethodInterceptor = resolvePointCut(methodMap);
        ByteBuddyInterceptorAdaptor commonInterceptorAdaptor = new ByteBuddyInterceptorAdaptor(multiMethodInterceptor);
        return commonInterceptorAdaptor;
    }

    private Interceptor resolvePointCut(Map<String, List<CutPoint>> methodMap) {
        Map<String, List<Interceptor>> map = methodMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> cutPoint2Interceptor(entry.getValue())));
        return new MultiMethodInterceptorGroup(map);
    }

    private List<Interceptor> cutPoint2Interceptor(List<CutPoint> cutPoints) {
        return cutPoints.stream().map(InterceptorRegistry.INSTANCE::newInterceptor).collect(Collectors.toList());
    }


    private ElementMatcher<TypeDescription> buildTypeMatcher(Map<String, Map<String, List<CutPoint>>> cutPoints) {
        return multiNameMatcher(cutPoints.keySet());
    }

    private ElementMatcher multiNameMatcher(Collection<String> typeNames) {
        ElementMatcher matcher = ElementMatchers.none();
        for (String typeName : typeNames) {
            matcher = named(typeName).or(matcher);
        }
        return matcher;
    }

}
