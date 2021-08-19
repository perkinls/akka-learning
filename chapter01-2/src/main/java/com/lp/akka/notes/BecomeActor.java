package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Procedure;

/**
 * @author li.pan
 * @title Actor 通过become切换行为
 */
public class BecomeActor extends UntypedActor {
    /**
     * 定义处理过程
     */
    Procedure<Object> procedure = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            System.out.println("become:" + param);
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("接收消息：" + message);
        // 接收到消息后切换到事先定义的处理过程
        getContext().become(procedure);
        System.out.println("----------------------");
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        ActorRef ref = system.actorOf(Props.create(BecomeActor.class), "becomeActor");
        ref.tell("hello", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
    }
}
