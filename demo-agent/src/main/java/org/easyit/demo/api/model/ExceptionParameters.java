package org.easyit.demo.api.model;

public class ExceptionParameters extends Parameters {

    private final Throwable throwable;

    public ExceptionParameters(Parameters b, Throwable throwable) {
        super(b.baseParameters);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
