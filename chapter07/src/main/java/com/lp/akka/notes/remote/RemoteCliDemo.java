package com.lp.akka.notes.remote;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;


/**
 * @author li.pan
 * @title Akka remote 启动默认端口2552
 */
public class RemoteCliDemo extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("Receive msg: "  + message);
        getSender().tell("HaHahaha", getSelf());
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys", ConfigFactory.load("application.conf"));

         system.actorOf(Props.create(RemoteCliDemo.class), "remoteActor");
    }
}
