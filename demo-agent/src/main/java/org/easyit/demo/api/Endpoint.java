package org.easyit.demo.api;

import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;


public interface Endpoint {

    String getName();

    default void onTaskBuild() {
    }

    default void onSpanStart(Parameters parameters, Context context) {
    }

    default void onSpanEnd(ReturnParameters returnParameters, Context context) {
    }

    default void onException(ExceptionParameters exceptionParameters, Context context) {
    }

    default void onSegmentStart() {
    }

    default void onSegmentEnd() {
    }

    default void onTraceStart() {
    }

    default void onTraceEnd() {
    }


}
