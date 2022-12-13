package org.easyit.demo.boot;

import java.net.URL;
import java.net.URLClassLoader;

public class AgentClassloader extends URLClassLoader {
    public AgentClassloader(URL[] urls) {
        // 注意: 这里的 parentClassloader 是 systemClassLoader 的parent ,也就是
        // DemoClassloader 和 SystemClassLoader 是平级的
        super(urls, ClassLoader.getSystemClassLoader().getParent());
    }

    // 这个重载只是为了优化性能,没什么功能性的东西
    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        // todo 这里的parent 应该是  SystemClassLoader 的 parent 吧 debug 看下
        // 优先从parent（SystemClassLoader）里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
            return super.loadClass(name, resolve);
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            // ignore
        }
        return super.loadClass(name, resolve);
    }
}
