package org.easyit.demo.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.easyit.demo.endpoint.EndpointManager;

import java.util.Objects;

import static com.alibaba.ttl.TransmittableThreadLocal.Transmitter.registerTransmittee;

/**
 * Life cycle of Trace ID:
 * Trace ID is created at it is first used. Typically, it is created at Trace first start.
 */
public class IdHolder {
    private static final ThreadLocal<Id> TRACE_ID = new ThreadLocal<>();
    private static final ThreadLocal<Id> SPAN_ID = ThreadLocal.withInitial(IdHolder::currentId);
    private static final TransmittableThreadLocal.Transmitter.Transmittee<Id, Id> traceIdTransmittee = new TransmittableThreadLocal.Transmitter.Transmittee<Id, Id>() {

        @Override
        public Id capture() {
            Id id = TRACE_ID.get();
            if (Objects.nonNull(id)) {
                id.increment();
                return id;
            }
            return null;
        }

        @Override
        public Id replay(Id captured) {
            if (Objects.nonNull(captured)) {
                TRACE_ID.set(captured);
            }
            return null;
        }

        @Override
        public Id clear() {
            return null;
        }

        @Override
        public void restore(Id backup) {
            Id id = TRACE_ID.get();
            if (Objects.nonNull(id)) {
                id.decrement();
            }
            removeAll();
        }
    };


    static {
        registerTransmittee(traceIdTransmittee);
    }

    private static Id currentId() {
        return new Id();
    }

    public static Id getTraceId() {
        Id id = TRACE_ID.get();
        if (Objects.isNull(id)) {
            id = new Id();
            TRACE_ID.set(id);
        }
        return id;
    }

    public static Id getSpanId() {
        return SPAN_ID.get();
    }

    public static void removeAll() {
        TRACE_ID.remove();
        SPAN_ID.remove();
    }

    public static void start() {
        Id traceId = TRACE_ID.get();
        Id spanId = SPAN_ID.get();
        if (Objects.isNull(traceId)) {
            traceId = new Id();
            TRACE_ID.set(traceId);
            traceId.addCallback(EndpointManager.INSTANCE::onTraceEnd);
            if (Objects.nonNull(spanId)) {
                throw new IllegalStateException("TraceId is null,SpanId must be null. However SpanId is not null now.");
            }
            EndpointManager.INSTANCE.onTraceStart();
        }

        if (Objects.isNull(spanId)) {
            // TraceId is not null, but spanId is null
            traceId.increment();
            spanId = new Id();
            spanId.addCallback(EndpointManager.INSTANCE::onSegmentEnd);
            spanId.addCallback(traceId::decrement);
            spanId.addCallback(IdHolder::removeAll);
            SPAN_ID.set(spanId);
            EndpointManager.INSTANCE.onSegmentStart();
        }

        // traceId and spanId are not null
        spanId.increment();

    }

    public static void end() {
        Id traceId = TRACE_ID.get();
        Id spanId = SPAN_ID.get();
        if (Objects.isNull(traceId) || Objects.isNull(spanId)) {
            throw new IllegalStateException("TraceId and SpanId cannot be null.");
        }
        spanId.decrement();
    }
}
