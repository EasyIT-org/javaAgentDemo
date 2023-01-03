package org.easyit.demo.endpoint;

import org.easyit.demo.api.Endpoint;
import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public enum EndpointManager implements Endpoint {
    INSTANCE;

    public ConcurrentHashMap<String, Endpoint> endpoints = new ConcurrentHashMap<>(16);

    EndpointManager() {
        register(ProfilerEndpoint.INSTANCE);
        // TODO: 2022/12/23 load endpoint from file
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onException(ExceptionParameters exceptionParameters, Context context) {
        execute(endpoint -> endpoint.onException(exceptionParameters, context));
    }

    @Override
    public void onSegmentEnd() {
        execute(Endpoint::onSegmentEnd);
    }

    @Override
    public void onSpanStart(Parameters parameters, Context context) {
        execute(endpoint -> endpoint.onSpanStart(parameters, context));
    }

    @Override
    public void onSpanEnd(ReturnParameters returnParameters, Context context) {
        execute(endpoint -> endpoint.onSpanEnd(returnParameters, context));
    }

    private void execute(Consumer<Endpoint> consumer) {
        for (Endpoint endpoint : endpoints.values()) {
            try {
                consumer.accept(endpoint);
            } catch (Throwable t) {
                // TODO: 2023/1/1 log and continue
            }
        }
    }

    public void register(String name, Endpoint endpoint) {
        endpoints.putIfAbsent(name, endpoint);
    }

    public void register(Endpoint endpoint) {
        endpoints.putIfAbsent(endpoint.getName(), endpoint);
    }
}
