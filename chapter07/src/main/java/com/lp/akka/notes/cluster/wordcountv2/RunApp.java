package com.lp.akka.notes.cluster.wordcountv2;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author li.pan
 * @title
 */
public class RunApp {
    public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");

            ActorRef ref = system.actorOf(Props.create(ClusterPoolRouter.class), "statsService");

        }

}
