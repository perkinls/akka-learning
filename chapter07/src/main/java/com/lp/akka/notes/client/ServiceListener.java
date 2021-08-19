package com.lp.akka.notes.client;

import akka.actor.UntypedActor;
import akka.cluster.client.ClusterClientUnreachable;
import akka.cluster.client.ClusterClientUp;
import akka.cluster.client.ClusterClients;

/**
 * @author li.pan
 * @title 监听ClusterClient信息的Actor
 */
public class ServiceListener extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception, Exception {
        if (message instanceof ClusterClients) {
            System.out.println("ClusterClients:" + message);
        } else if (message instanceof ClusterClientUp) {
            System.out.println("ClusterClientUp:" + message);
        } else if (message instanceof ClusterClientUnreachable) {
            System.out.println("ClusterClientUnreachable:" + message);
        }
    }
}
