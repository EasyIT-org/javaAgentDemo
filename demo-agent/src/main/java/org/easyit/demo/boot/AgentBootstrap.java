package org.easyit.demo.boot;

import java.DemoSpy;
import java.io.File;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class AgentBootstrap {

    public static final String DEMO_CORE_JAR = "demo-agent-1.0-SNAPSHOT-jar-with-dependencies.jar";
    private static final String DEMO_BOOTSTRAP = "org.easyit.demo.boot.DemoBootstrap";
    private static final String GET_INSTANCE = "getInstance";
    private static final String IS_INITIALIZED = "isInitialized";

    private static volatile AgentClassloader agentClassLoader;

    private static final PrintStream ps = System.err;


    public static void premain(String args, Instrumentation inst) {
        recoverMain(inst);
    }


    public static void agentmain(String args, Instrumentation inst) {
        recoverMain(inst);
    }


    private static void recoverMain(Instrumentation inst) {
        new ByteBuddyStarter().start(inst);
//        try {
//            System.out.println("start main");
//            main(inst);
//        } catch (Throwable t) {
//            t.printStackTrace(ps);
//            try {
//                if (ps != System.err) {
//                    ps.close();
//                }
//            } catch (Throwable tt) {
//                // ignore
//            }
//            throw new RuntimeException(t);
//        }
    }

    private static void main(final Instrumentation inst) throws Throwable {

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                System.out.println("perform transform");
                return null;
            }
        });

        AgentBootstrap agentBootstrap = new AgentBootstrap();
        System.out.println(agentBootstrap.getClass().getClassLoader());
        // 1. check double init
        try {
            Class.forName("java.DemoSpy"); // 加载不到会抛异常
            if (DemoSpy.isInited()) {
                ps.println("Demo agent already stared, skip attach.");
                ps.flush();
                return;
            }
        } catch (Throwable e) {
            // ignore
        }

        // 2. find agent jar file
        File agentJarFile = null;
        CodeSource codeSource = AgentBootstrap.class.getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            try {
                File demoAgentJarFile = new File(codeSource.getLocation().toURI().getSchemeSpecificPart());
                agentJarFile = new File(demoAgentJarFile.getParentFile(), DEMO_CORE_JAR);
                if (!agentJarFile.exists()) {
                    ps.println("Can not find demo-agent jar file from agent jar directory: " + demoAgentJarFile);
                }
            } catch (Throwable e) {
                ps.println("Can not find demo-agent jar file from " + codeSource.getLocation());
                e.printStackTrace(ps);
            }
        }

        if (agentJarFile == null || !agentJarFile.exists()) {
            return;
        }

        // 3. init classloader
        final ClassLoader agentLoader = getClassLoader(agentJarFile);


        // 4. init and wait
        Thread bindingThread = new Thread() {
            @Override
            public void run() {
                try {
                    init(inst, null);
                } catch (Throwable throwable) {
                    throwable.printStackTrace(ps);
                }
            }
        };

        bindingThread.setName("demo-init-thread");
        bindingThread.start();
        bindingThread.join();
    }

    private static void init(Instrumentation inst, ClassLoader agentLoader) throws Throwable {
//        Class<?> bootstrapClass = agentLoader.loadClass(DEMO_BOOTSTRAP);
        Class<?> bootstrapClass = Class.forName(DEMO_BOOTSTRAP);
        Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class).invoke(null, inst);
        boolean isInitialized = (Boolean) bootstrapClass.getMethod(IS_INITIALIZED).invoke(bootstrap);
        if (!isInitialized) {
            String errorMsg = "Initialized failed.";
            ps.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }


    private static ClassLoader getClassLoader(File coreJarFile) throws Throwable {
        // 构造自定义的类加载器，尽量减少Agent对现有工程的侵蚀
        return loadOrDefineClassLoader(coreJarFile);
    }

    private static ClassLoader loadOrDefineClassLoader(File agentCoreJarFile) throws Throwable {
        if (agentClassLoader == null) {
            agentClassLoader = new AgentClassloader(new URL[]{agentCoreJarFile.toURI().toURL()});
        }
        return agentClassLoader;
    }


}
