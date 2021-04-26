package com.lp.akka.notes

import akka.actor.{ActorSystem, Props}

/**
 * @title TODO
 * @author li.pan
 * @version 1.0.0
 * @createTime 2021年04月16日 12:29:00
 *             <p>
 *             $description
 *             </p>
 */
object RunApp extends App {
  val system: ActorSystem = ActorSystem.create("akkademy")
  system.actorOf(Props[AkkademyDb], "akkademy-db")
}
