package com.lp.akka.notes

import akka.actor.{ActorSystem, Props}
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.routing.BalancingPool

/**
 * @title TODO
 * @author li.pan
 * @version 1.0.0
 * @createTime 2021年04月30日 14:42:00
 *             <p>
 *             使用Router Pool 中用于解析文章的 Actor 放到集群上，然后启动集群。
 *             启动完成后，我们就将介绍如何从另一个 Actor 系统访问集群中的服务
 *             </p>
 */
object ParseArticleApp extends App {

  val system = ActorSystem("Akkademy")
  val clusterController = system.actorOf(Props[ClusterController], "clusterController")

  // router at /user/workers
  val workers =
    system.actorOf(BalancingPool(5).props(Props[ArticleParseActor]), "workers")

  // 在 ClusterReceptionist 中注册 worker Actor
  ClusterReceptionistExtension(system).registerService(workers)
}
