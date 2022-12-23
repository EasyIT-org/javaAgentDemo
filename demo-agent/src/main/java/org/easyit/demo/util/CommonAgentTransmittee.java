package org.easyit.demo.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.eventbus.EventBus;

public class CommonAgentTransmittee implements TransmittableThreadLocal.Transmitter.Transmittee {

    public static void injectCommonAgentTransmittee() {
        TransmittableThreadLocal.Transmitter.registerTransmittee(new CommonAgentTransmittee());
    }

    @Override
    public Object capture() {
        EventBus.
        return null;
    }

    @Override
    public Object replay(Object captured) {
        return null;
    }

    @Override
    public Object clear() {
        return null;
    }

    @Override
    public void restore(Object backup) {

    }
}
