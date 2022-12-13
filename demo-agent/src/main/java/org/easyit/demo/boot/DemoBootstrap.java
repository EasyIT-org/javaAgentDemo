package org.easyit.demo.boot;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.security.CodeSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;

public class DemoBootstrap {
    public static final String SPY_CLASS_NAME = "java.DemoSpy";
    private static DemoBootstrap demoBootstrap;


    private final AtomicBoolean initedRef = new AtomicBoolean(false);
    private final Instrumentation instrumentation;

    private final Thread shutdown;

    private final ScheduledExecutorService executorService;

    public DemoBootstrap(Instrumentation instrumentation) throws Throwable {
        System.out.println("init DemoBootstrap");
        this.instrumentation = instrumentation;

        if (!initedRef.compareAndSet(false, true)) {
            throw new IllegalStateException("Already initialized.");
        }
        System.out.print("initedRef set");

        // 1. init spy
        System.out.println("init spy");
        initSpy();
        // 2. init configuration
        // TODO: 2022/11/11
        // 3. init log
        // TODO: 2022/11/11
        // 4. enhance ClassLoader
        // TODO: 2022/11/11

        // 5. init beans
        // TODO: 2022/11/11
//        initBeans();

        // 6. start agent server
        // TODO: 2022/11/11
//        bind(configure);

        // 7 init thread pool
        System.out.println("init thread pool");
        executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final Thread t = new Thread(r, "demo-command-execute");
                t.setDaemon(true);
                return t;
            }
        });

        // 8. add shutdown hook
        System.out.println("add shutdown hook");
        shutdown = new Thread("demo-shutdown-hooker") {

            @Override
            public void run() {
                DemoBootstrap.this.destroy();
            }
        };

        Runtime.getRuntime().addShutdownHook(shutdown);


        // 8 do business logic
        System.out.println("do business logic");

        doBusiness(instrumentation);
    }

    /**
     * 单例
     *
     * @param instrumentation JVM增强
     * @return demoServer单例
     * @throws Throwable
     */
    public synchronized static DemoBootstrap getInstance(Instrumentation instrumentation) throws Throwable {
        if (demoBootstrap == null) {
            demoBootstrap = new DemoBootstrap(instrumentation);
        }
        return demoBootstrap;
    }

    private void doBusiness(Instrumentation instrumentation) {
        new ByteBuddyStarter().start(instrumentation);
    }

    private void destroy() {
        // TODO: 2022/11/11
    }

    private void initSpy() throws Throwable {
        // TODO init SpyImpl ?

        // 将Spy添加到BootstrapClassLoader
        ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
        Class<?> spyClass = null;
        if (parent != null) {
            try {
                spyClass = parent.loadClass(SPY_CLASS_NAME);
            } catch (Throwable e) {
                // ignore
            }
        }
        if (spyClass == null) {
            // todo  在 arthas 中, spy class 和其他的包不在同一个jar 中, 因此arths 只将 spy jar 添加到了bootstrap class loader 中
            // 我们这里都打到一个包里面, 就会将所有class 都放到 bootstrap classloader 中, 这个后续要看下有没有什么副作用.
            CodeSource codeSource = DemoBootstrap.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                File demoCoreJarFile = new File(codeSource.getLocation().toURI().getSchemeSpecificPart());
                File spyJarFile = new File(demoCoreJarFile.getParentFile(), AgentBootstrap.DEMO_CORE_JAR);
                instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(spyJarFile));
            } else {
                throw new IllegalStateException("can not find " + AgentBootstrap.DEMO_CORE_JAR);
            }
        }
    }

    public boolean isInitialized() {
        return initedRef.get();
    }


}
