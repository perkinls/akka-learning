package com.lp.akka.notes;

import akka.actor.*;
import akka.japi.Creator;

/**
 * @author li.pan
 * @version 1.0.0
 * @title 基于工厂模式实现获取Actor
 */
public class PropsDemoActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {

    }

    /**
     * 静态工厂方式
     *
     * @return Props
     */
    public static Props createProps() {
        return Props.create(new Creator<Actor>() {
            @Override
            public PropsDemoActor create() throws Exception {
                return new PropsDemoActor();
            }
        });
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        ActorRef ref = system.actorOf(PropsDemoActor.createProps(), "propsActor");
    }
}
