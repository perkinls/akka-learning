package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.japi.LookupEventBus;

/**
 * @author li.pan
 * @title Akka 中事件总线
 * 范型Event, ActorRef, String 分别代表 事件类型、订阅类型、Classify类型
 */
public class EventBusActor extends LookupEventBus<Event, ActorRef, String> {


    /**
     * 订阅类型
     * @param event
     * @return
     */
    @Override
    public String classify(Event event) {
        return event.getType();
    }

    /**
     * 具体的订阅
     * @param a
     * @param b
     * @return
     */
    @Override
    public int compareSubscribers(ActorRef a, ActorRef b) {
        return a.compareTo(b);
    }

    /**
     * 发布事件
     * @param event
     * @param ref
     */
    @Override
    public void publish(Event event, ActorRef ref) {
        ref.tell(event.getMessage(), ActorRef.noSender());
    }

    /**
     * 期望classify数,一般设置为2的n次幂
     * @return
     */
    @Override
    public int mapSize() {
        return 8;
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef eventSubActor = actorSystem.actorOf(Props.create(EventSubActor.class), "eventSubActor");

        EventBusActor bus = new EventBusActor();
        bus.subscribe(eventSubActor, "info");
        bus.subscribe(eventSubActor, "warn");

        bus.publish(new Event("info", "Hello EventBus"));

        bus.publish(new Event("warn", "Oh No"));

        bus.unsubscribe(eventSubActor, "warn");

        bus.publish(new Event("warn", "Oh No Again"));
    }
}

class EventSubActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception, Exception {
        System.out.println(message);
    }
}


class Event {
    private String type;

    private String message;

    public Event(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Event(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
