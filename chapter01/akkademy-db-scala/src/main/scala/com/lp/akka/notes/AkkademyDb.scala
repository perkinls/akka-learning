package com.lp.akka.notes

import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable.HashMap

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 创建 Actor，并描述 Actor 接收到消息后如何做出响应
 * @createTime 2020年12月16日 20:37:00
 */
class AkkademyDb extends Actor {

  val map = new HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) => {
      log.info("received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
    }
    case o => log.info("received unknown message: {}", o);
  }

}
