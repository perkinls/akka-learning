package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * 启动主类
 * @author lipan
 */
public class Main {
    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("akkademy");
        ActorRef actor = system.actorOf(Props.create(AkkademyDb.class), "akkademy-db");
    }
}
