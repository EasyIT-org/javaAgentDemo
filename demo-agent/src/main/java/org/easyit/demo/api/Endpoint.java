package org.easyit.demo.api;

import org.easyit.demo.api.model.Context;
import org.easyit.demo.api.model.ExceptionParameters;
import org.easyit.demo.api.model.Parameters;
import org.easyit.demo.api.model.ReturnParameters;


public interface Endpoint {

    String getName();

    default void onTaskBuild() {
    }

    default void onTaskStart() {
    }

    default void onTaskEnd() {
    }

    default void onTraceStart(Parameters parameters, Context context) {
    }

    default void onTraceEnd(ReturnParameters returnParameters, Context context) {
    }

    default void onException(ExceptionParameters exceptionParameters, Context context) {
    }

    default void report() {
    }
}
