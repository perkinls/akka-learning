package com.lp.akka.notes.cluster;

import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li.pan
 * @title 订阅集群相关事件，集群的状态发生变化的时候可以感知到
 */
public class SimpleClusterListener extends UntypedActor {
    private static final Logger log = LoggerFactory.getLogger(SimpleClusterListener.class);

    Cluster cluster = Cluster.get(getContext().system());


    /**
     * 订阅集群变换事件
     */
    @Override
    public void preStart() {
        // 订阅集群多个状态
        cluster.subscribe(getSelf(),
                ClusterEvent.initialStateAsEvents(),
                ClusterEvent.MemberEvent.class,
                ClusterEvent.UnreachableMember.class);
    }


    /**
     * Actor重启时取消订阅
     */
    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp mUp = (ClusterEvent.MemberUp) message;
            log.info("Member is Up: {}", mUp.member());

        } else if (message instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember mUnreachable = (ClusterEvent.UnreachableMember) message;
            log.info("Member detected as unreachable: {}", mUnreachable.member());

        } else if (message instanceof ClusterEvent.MemberRemoved) {
            ClusterEvent.MemberRemoved mRemoved = (ClusterEvent.MemberRemoved) message;
            log.info("Member is Removed: {}", mRemoved.member());

        } else if (message instanceof ClusterEvent.MemberEvent) {
            // ignore

        } else {
            unhandled(message);
        }

    }


    public static void main(String[] args) {
        String port = args[0];
        ActorSystem system = ActorSystem.create("sys",
                ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                        .withFallback(ConfigFactory.load("cluster-listener-application.conf")));
        system.actorOf(Props.create(SimpleClusterListener.class), "clusterListner");
    }
}

class JoinCluster {
    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("sys",
                ConfigFactory.parseString("akka.remote.netty.tcp.port=2555")
                        .withFallback(ConfigFactory.load("cluster-listener-application.conf")));

        system.actorOf(Props.create(SimpleClusterListener.class), "Join_cluster");
        Cluster cluster = Cluster.get(system);
        // 向集群阶段2552端口发送join请求
        Address address = new Address("akka.tcp", "sys", "127.0.0.1", 2552);
        cluster.join(address);

    }
}

class ClusterStatus {
    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("sys",
                ConfigFactory.parseString("akka.remote.netty.tcp.port=2556")
                        .withFallback(ConfigFactory.load("cluster-listener-application.conf")));
        ClusterEvent.CurrentClusterState state = Cluster.get(system).state();

        System.out.println("Leader节点:"+state.getLeader());
        System.out.println("所有节点列表:"+state.getMembers());
        System.out.println("unreachable节点列表:"+state.getUnreachable());


    }
}