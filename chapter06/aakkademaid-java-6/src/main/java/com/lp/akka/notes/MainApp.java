package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.contrib.pattern.ClusterReceptionistExtension;
import akka.routing.BalancingPool;

/**
 * Actor系统启动入口
 * 可以通过-Dakka.remote.netty.tcp.port=2551 在同一机器上启动多个实例。（指定为 0，由 Akka 来随机分配一个端口）
 */
public class MainApp {
    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("Akkademy");
        ActorRef clusterController = system.actorOf(Props.create(ClusterController.class), "clusterController");

    }
}
