package org.easyit.demo.api.model;

public class ReturnParameters extends Parameters {
    private final Object ret;

    public ReturnParameters(Parameters p, Object ret) {
        super(p.baseParameters);
        this.ret = ret;
    }

    public Object getRet() {
        return ret;
    }
}
