package org.easyit.demo.event;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventBus {

    private static final Map<Event, Set<Subscriber>> SUBSCRIBERS = new ConcurrentHashMap<>();

    public static void register(Event event, Subscriber subscriber) {
        Set<Subscriber> subscribers = SUBSCRIBERS.computeIfAbsent(event, EventBus::createMap);
        subscribers.add(subscriber);
    }

    public static void unregister(Event event, Subscriber subscriber) {
        Set<Subscriber> subscribers = SUBSCRIBERS.computeIfAbsent(event, EventBus::createMap);
        subscribers.remove(subscriber);
    }

    private static Set<Subscriber> createMap(Event event) {
        return new CopyOnWriteArraySet<>();
    }
}
