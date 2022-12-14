package org.easyit.demo.api.model;

import org.easyit.demo.api.CutPoint;

public class Context {
    private CutPoint cutPoint;

    public Context(CutPoint cutPoint) {
        this.cutPoint = cutPoint;
    }

    public CutPoint getCutPoint() {
        return cutPoint;
    }
}
