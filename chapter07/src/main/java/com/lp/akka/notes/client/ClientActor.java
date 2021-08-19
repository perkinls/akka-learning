package com.lp.akka.notes.client;

import akka.actor.ActorPath;
import akka.actor.ActorPaths;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author li.pan
 * @title
 */
public class ClientActor {

    public static void main(String[] args) {
        // 提前启动集群 带akka.cluster.client.ClusterClientReceptionist属性
        Set<ActorPath> initContacts = new HashSet<>(Arrays.asList(
                ActorPaths.fromString("akka.tcp://sys@127.0.0.1:2550/system/receptionist"),
                ActorPaths.fromString("akka.tcp://sys@127.0.0.1:2551/system/receptionist")
        ));
        ActorSystem system = ActorSystem.create("sys1", ConfigFactory.load("clustercli.conf"));
        ActorRef c = system.actorOf(
                ClusterClient.props(
                        ClusterClientSettings.create(system).withInitialContacts(initContacts)), // 明确指定contact-points节点
                "client");
        c.tell(new ClusterClient.Send("/user/wordFrontService", "hello", true), ActorRef.noSender());
    }
}
