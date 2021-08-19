package com.lp.akka.notes.remote;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title Akka remote
 */
public class RemoteActor3 extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("RemoteActor3: " +message);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys", ConfigFactory.load("remote3-application.conf"));
        ActorRef router = system.actorOf(FromConfig.getInstance().props(), "rmtCommon");
        router.tell("hello rmt", ActorRef.noSender());
    }
}
