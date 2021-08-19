package com.lp.akka.notes.cluster.wordcountv1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * @author li.pan
 * @title 启动后端程序入口类
 */
public class StartBackendApp3 {
    public static void main(String[] args) {
        String port = "2553";
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.load("cluster-word-count-v1.conf"));
        ActorSystem system = ActorSystem.create("sys", config);
        ActorRef ref = system.actorOf(Props.create(WordCountService.class), "wordCountService" + port);

    }
}
