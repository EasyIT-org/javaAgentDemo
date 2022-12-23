package org.easyit.demo.api.model;

import java.util.concurrent.atomic.LongAdder;

public class TraceState {

    private LongAdder childrenThreadCount;
    private Object rootElement;
    private Object firstElement;

    public TraceState() {
        this.childrenThreadCount = new LongAdder();
    }

    public LongAdder getChildrenThreadCount() {
        return childrenThreadCount;
    }

    public void setChildrenThreadCount(LongAdder childrenThreadCount) {
        this.childrenThreadCount = childrenThreadCount;
    }

    public Object getRootElement() {
        return rootElement;
    }

    public void setRootElement(Object rootElement) {
        this.rootElement = rootElement;
    }

    public Object getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(Object firstElement) {
        this.firstElement = firstElement;
    }
}
