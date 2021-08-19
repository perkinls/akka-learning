package com.lp.akka.notes.remote;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title Akka remote
 */
public class RemoteActor2 extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("RemoteActor2: " +message);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys", ConfigFactory.load("remote2-application.conf"));
        system.actorOf(Props.create(RemoteActor2.class), "rmt2");
    }
}
