package com.lp.akka.notes.cluster.wordcountv3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.client.ClusterClientReceptionist;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title 启动后端程序入口类
 */
public class StartBackendApp2 {
    public static void main(String[] args) {
        String port = "2552";
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.load("cluster-word-count-v1.conf"));
        ActorSystem system = ActorSystem.create("sys", config);
        ActorRef ref = system.actorOf(Props.create(WordCountService.class), "wordCountService" + port);

        /*
         * 通过ClusterSingletonManagerSettings创建集群单例对象
         * 单例容易产生单点问题,切忌不要滥用
         */
        ClusterSingletonManagerSettings settings = ClusterSingletonManagerSettings.create(system);
        system.actorOf(
                ClusterSingletonManager.props(Props.create(SingletonActor.class),
                        PoisonPill.getInstance(),
                        settings),
                "singleActor");

        /*
         * 注册receptionist,在集群每个节点自动生成
         */
        ClusterClientReceptionist.get(system).registerService(ref);
    }
}
