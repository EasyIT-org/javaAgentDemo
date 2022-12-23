package org.easyit.demo.event;

public interface Event {


    enum Default implements Event {
        TaskCreate,
        TaskStart,
        TaskEnd,
        TransactionStart,
        TransactionEnd
    }
}
