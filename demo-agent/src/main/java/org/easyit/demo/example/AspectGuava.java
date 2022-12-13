package org.easyit.demo.example;

import com.taobao.arthas.core.util.SearchUtils;
import org.easyit.demo.api.Extension;

import java.lang.instrument.Instrumentation;
import java.util.Set;

public class AspectGuava implements Extension, Runnable {
    private Instrumentation inst;


    public AspectGuava(Instrumentation inst) {
        System.out.println("new  AspectGuava");
        this.inst = inst;
    }

    @Override
    public void run() {
        Set<Class<?>> classes = SearchUtils.searchClass(inst, "org.example.SleepList", false);
        for (Class<?> aClass : classes) {
            System.out.println(aClass.getName());
        }
    }
}
