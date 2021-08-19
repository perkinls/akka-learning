package com.lp.akka.notes.client;

import akka.actor.UntypedActor;
import akka.cluster.client.ContactPointAdded;
import akka.cluster.client.ContactPointRemoved;
import akka.cluster.client.ContactPoints;

/**
 * @author li.pan
 * @title 监听ClusterClientReceptionist消息
 */
public class ClientListener extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception, Exception {
        if (message instanceof ContactPoints) {
            System.out.println("ContactPoints:" + message);
        } else if (message instanceof ContactPointAdded) {
            System.out.println("ContactPointAdded:" + message);
        } else if (message instanceof ContactPointRemoved) {
            System.out.println("ContactPointRemoved:" + message);
        }
    }
}