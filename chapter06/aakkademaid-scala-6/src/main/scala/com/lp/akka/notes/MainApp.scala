package com.lp.akka.notes

import akka.actor.{ActorSystem, Props}

/**
 * @title TODO
 * @author li.pan
 * @version 1.0.0
 * @createTime 2021年04月30日 14:42:00
 *             <p>
 *             $description
 *             </p>
 */
object MainApp extends App {
  val system = ActorSystem("Akkademy")

  val clusterController = system.actorOf(Props[ClusterController], "clusterController")
}
