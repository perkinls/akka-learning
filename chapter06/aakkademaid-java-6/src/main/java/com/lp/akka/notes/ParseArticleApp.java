package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.contrib.pattern.ClusterReceptionistExtension;
import akka.routing.BalancingPool;

/**
 *  使用Router Pool 中用于解析文章的 Actor 放到集群上，然后启动集群。
 *  启动完成后，我们就将介绍如何从另一个 Actor 系统访问集群中的服务
 */
public class ParseArticleApp {
    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("lp-akka-cluster");

        // 使用BalancingPool构建多个routee   // router at /user/workers
        ActorRef workers =
                system.actorOf(
                        new BalancingPool(5).props(Props.create(ArticleParseActor.class)),
                        "workers"
                );

        // 在 ClusterReceptionist 中注册 worker Actor
        ((ClusterReceptionistExtension) akka.contrib.pattern.ClusterReceptionistExtension.apply(system)).
                registerService(workers);
    }
}
