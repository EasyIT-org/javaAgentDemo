package org.easyit.demo.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import org.easyit.demo.api.model.Config;
import org.easyit.demo.bytebuddy.ByteBuddyInterceptorAdaptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.hasSuperClass;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class ByteBuddyStarter {

    public void start(Instrumentation instrumentation) {
        List<CutPoint> cutPoints = loadCutPoints();
        Map<String, Map<String, List<CutPoint>>> cutPointMap = parseCommonCutPoints(cutPoints);
        Map<String, List<CutPoint>> specialCutPoint = cutPoints.stream().filter(cutPoint -> Objects.nonNull(cutPoint.getEnhanceType())).collect(Collectors.groupingBy(CutPoint::getEnhanceType));


        AgentBuilder.Identified.Extendable extendable = new AgentBuilder.Default().with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(multiNameMatcher(cutPoints))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, ProtectionDomain protectionDomain) {

                        // todo 现在这里的 类-方法-interceptor 的关系是 1:N:1 ,在interceptor中又存放一个 map 来确定最终的 interceptor
                        // todo 需要考虑下,是否把这种关系改为 1:N:N,在构造之初,就把方法对应的 interceptor 确定下来
                        Map<String, List<CutPoint>> methodMap = cutPointMap.get(typeDescription.getName()); // todo debug 看下这边的 typeDescription.getName()
                        if (Objects.nonNull(methodMap)) {
                            DynamicType.Builder<?> tmp = builder;
                            for (Map.Entry<String, List<CutPoint>> methodCutPointEntry : methodMap.entrySet()) {
                                tmp = tmp.method(named(methodCutPointEntry.getKey()))
                                        .intercept(MethodDelegation.withDefaultConfiguration().to(getInterceptorByCutPoint(methodCutPointEntry.getValue())));
                            }
                            return tmp;
                        } else {
                            List<CutPoint> abstractCutPoint = specialCutPoint.get("abstract");
                            for (CutPoint cutPoint : abstractCutPoint) {
                                if (ElementMatchers.hasSuperClass(named(cutPoint.getClassName())).matches(typeDescription)) {
                                    DynamicType.Builder<?> tmp = builder;
                                    ArrayList<CutPoint> singleCutPoint = new ArrayList<>();
                                    singleCutPoint.add(cutPoint);
                                    tmp = tmp.method(named(cutPoint.getMethodName())).
                                            intercept(MethodDelegation.withDefaultConfiguration().to(getInterceptorByCutPoint(singleCutPoint)));
                                    return tmp;
                                }
                            }
                        }
                        // todo log error
                        throw new IllegalStateException();
                    }
                });
        extendable.installOn(instrumentation);
    }

    private ByteBuddyInterceptorAdaptor getInterceptorByCutPoint(List<CutPoint> cutPoints) {
        List<Interceptor> interceptorList = cutPoint2Interceptor(cutPoints);
        ListInterceptorGroup listInterceptorGroup = new ListInterceptorGroup(interceptorList);
        return new ByteBuddyInterceptorAdaptor(listInterceptorGroup);
    }

    private Map<String, Map<String, List<CutPoint>>> parseCommonCutPoints(List<CutPoint> cutPoints) {
        return cutPoints.stream().filter(cutPoint -> Objects.isNull(cutPoint.getEnhanceType())).collect(Collectors.groupingBy(CutPoint::getClassName, Collectors.groupingBy(CutPoint::getMethodName)));


    }

    private List<CutPoint> loadCutPoints() {
        List<Config> configs = readConfigsFromFile();
        return configs.stream().map(Config::getCutPoints).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<Config> readConfigsFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("common-agent/config");
            InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String s = bufferedReader.readLine();
            List<String> lines = new ArrayList<>();
            while (s != null) {
                System.out.println(s);
                lines.add(s);
                s = bufferedReader.readLine();
            }
            bufferedReader.close();
            List<Config> configs = new ArrayList<>();
            for (String line : lines) {
                InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("common-agent/" + line);
                Config config = mapper.readValue(systemResourceAsStream, Config.class);
                configs.add(config);
                System.out.println(config.getVersion());
            }
            return configs;
        } catch (IOException ioException) {
            // TODO: 2023/1/4  log

            ioException.printStackTrace();
        }


        return null;
    }

    private List<Interceptor> cutPoint2Interceptor(List<CutPoint> cutPoints) {
        return cutPoints.stream().map(InterceptorRegistry.INSTANCE::newInterceptor).collect(Collectors.toList());
    }

    protected ElementMatcher<TypeDescription> multiNameMatcher(Collection<CutPoint> cutPoints) {
        ElementMatcher<TypeDescription> matcher = ElementMatchers.none();
        for (CutPoint cutPoint : cutPoints) {
            if ("abstract".equals(cutPoint.getEnhanceType())) {
                matcher = hasSuperClass(named(cutPoint.getClassName())).or(matcher);
            } else {
                matcher = named(cutPoint.getClassName()).or(matcher);
            }
        }
        return matcher;
    }

}
